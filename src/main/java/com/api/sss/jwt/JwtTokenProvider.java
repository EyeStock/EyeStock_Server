package com.api.sss.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
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
import io.jsonwebtoken.security.Keys;
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

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

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
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public Boolean validateOrThrow(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException ex) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException ex) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException ex) {
			throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (IllegalArgumentException ex) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

		String subject = claims.getSubject();
		String role = "ROLE_USER";

		User principal = new User(subject, "", Collections.singleton(() -> role));
		return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
	}
}
