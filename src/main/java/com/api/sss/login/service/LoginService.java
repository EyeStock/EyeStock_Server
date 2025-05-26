package com.api.sss.login.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.login.dto.BiometricLoginStartRequest;
import com.api.sss.login.dto.BiometricLoginStartResponse;
import com.api.sss.login.dto.BiometricSignupRequest;
import com.api.sss.member.entity.Member;
import com.api.sss.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;

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

}
