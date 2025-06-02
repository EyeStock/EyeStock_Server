package com.api.sss.jwt.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access-expiration}")
	private long accessTokenValidity;

	@Value("${jwt.refresh-expiration}")
	private long refreshTokenValidity;

	public String createAccessToken(Long userId) {
		return createToken(userId, accessTokenValidity);
	}

	public String createRefreshToken(Long userId) {
		return createToken(userId, refreshTokenValidity);
	}

	private String createToken(Long userId, long validity) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + validity);

		return Jwts.builder()
			.setSubject(userId.toString())
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	//JWT 안에 들어있는 사용자 식별자 (userId)를 꺼내기 위한 메소드
	public Long getUserId(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public void validateOrThrow(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException | MalformedJwtException |
				 SecurityException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}
}
