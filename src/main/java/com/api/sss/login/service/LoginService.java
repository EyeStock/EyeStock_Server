package com.api.sss.login.service;

import java.util.UUID;

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
		Member member = memberRepository.findByDeviceId(request.getDeviceId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		String challenge = UUID.randomUUID().toString();
		member.updateChallenge(challenge);

		return new BiometricLoginStartResponse(challenge);
	}

}
