package com.api.sss.login.service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.jwt.JwtTokenProvider;
import com.api.sss.login.dto.request.BiometricLoginStartRequest;
import com.api.sss.login.dto.response.BiometricLoginStartResponse;
import com.api.sss.login.dto.request.BiometricLoginVerifyRequest;
import com.api.sss.login.dto.response.BiometricLoginVerifyResponse;
import com.api.sss.login.dto.request.BiometricSignupRequest;
import com.api.sss.login.dto.request.RefreshTokenRequest;
import com.api.sss.login.dto.response.RefreshTokenResponse;
import com.api.sss.member.entity.Member;
import com.api.sss.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;

	private static final Duration CHALLENGE_TTL = Duration.ofMinutes(3);

	@Transactional
	public void signup(BiometricSignupRequest request) {
		if (memberRepository.findByDeviceId(request.getDeviceId()).isPresent()) {
			throw new CustomException(ErrorCode.DEVICE_ALREADY_REGISTERED);
		}

		Member member = Member.builder()
			.deviceId(request.getDeviceId())
			.publicKey(request.getPublicKey())
			.build();

		memberRepository.save(member);
	}

	@Transactional
	public BiometricLoginStartResponse startLogin(BiometricLoginStartRequest request) {
		boolean exists = memberRepository.existsByDeviceId(request.getDeviceId());
		if (!exists) {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}

		String challenge = UUID.randomUUID().toString();
		String redisKey = "challenge:" + request.getDeviceId();

		redisTemplate.opsForValue().set(redisKey, challenge, CHALLENGE_TTL);

		return new BiometricLoginStartResponse(challenge);
	}

	@Transactional
	public BiometricLoginVerifyResponse verifyLogin(BiometricLoginVerifyRequest request) {
		Member member = memberRepository.findByDeviceId(request.getDeviceId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		String redisKey = "challenge:" + request.getDeviceId();
		String storedChallenge = redisTemplate.opsForValue().get(redisKey);
		if (storedChallenge == null || !storedChallenge.equals(request.getChallenge())) {
			throw new CustomException(ErrorCode.INVALID_CHALLENGE);
		}

		boolean isVerified = verifySignature(member.getPublicKey(), request.getChallenge(), request.getSignature());
		if (!isVerified) {
			throw new CustomException(ErrorCode.INVALID_SIGNATURE);
		}

		// ✅ 성공했으면 challenge 제거
		redisTemplate.delete(redisKey);

		// ✅ JWT 발급
		String accessToken = jwtTokenProvider.createAccessToken(member.getId());
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

		// ✅ refreshToken Redis 저장 (key: refresh_token:{userId})
		redisTemplate.opsForValue().set(
			"refresh_token:" + member.getId(),
			refreshToken,
			Duration.ofDays(7)
		);

		return new BiometricLoginVerifyResponse(accessToken, refreshToken);
	}

	private boolean verifySignature(String base64PublicKey, String challenge, String base64Signature) {
		try {
			// 1. Base64로 인코딩된 공개키 → 바이트로 디코딩
			byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);

			// 2. 바이트 배열을 X509 형식 공개키 객체로 변환
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);

			// 3. Signature 객체 생성 (알고리즘은 SHA256withRSA)
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(publicKey);

			// 4. 검증할 데이터 설정 (challenge 문자열을 바이트로)
			sig.update(challenge.getBytes());

			// 5. 프론트에서 전달된 signature(Base64 인코딩)를 디코딩
			byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);

			// 6. 공개키로 서명 검증 (verify)
			return sig.verify(signatureBytes);
		} catch (Exception e) {
			return false; // 예외 발생 시 검증 실패로 처리
		}
	}

	@Transactional
	public RefreshTokenResponse reissue(RefreshTokenRequest request) {
		String refreshToken = request.getRefreshToken();

		jwtTokenProvider.validateOrThrow(refreshToken);

		Long userId = jwtTokenProvider.getUserId(refreshToken);

		String storedToken = redisTemplate.opsForValue().get("refresh_token:" + userId);
		if (storedToken == null || !storedToken.equals(refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		// 토큰 재발급
		String newAccessToken = jwtTokenProvider.createAccessToken(userId);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

		// 기존 refreshToken 제거 후 새로 저장 (갱신 방식)
		redisTemplate.delete("refresh_token:" + userId);
		redisTemplate.opsForValue().set(
			"refresh_token:" + userId,
			newRefreshToken,
			Duration.ofDays(7)
		);

		return new RefreshTokenResponse(newAccessToken, newRefreshToken);
	}


}
