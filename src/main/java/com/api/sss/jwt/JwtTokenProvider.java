package com.api.sss.jwt;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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

	public Boolean validateOrThrow(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT token");
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT token");
			throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty.");
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();

		String email = claims.getSubject();
//		String role = claims.get("role", String.class);
		String role = "ROLE_USER";

		User principal = new User(email, "", Collections.singleton(() -> role));
		return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
	}

}
