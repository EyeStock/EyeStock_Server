package com.api.sss.login.service;

import org.springframework.stereotype.Service;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.login.dto.BiometricSignupRequest;
import com.api.sss.member.entity.Member;
import com.api.sss.member.repository.MemberRepository;

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

}
