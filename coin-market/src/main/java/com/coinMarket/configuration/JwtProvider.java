package com.coinMarket.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
	private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	public static String generateToken(Authentication auth) {
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		String roles = populateAuthorities(authorities);
		String jwt = Jwts.builder()
			  .issuedAt(new Date())
			  .expiration(new Date(new Date().getTime() + 3600000))
			  .claim("email", auth.getName())
			  .claim("authorities", roles)
			  .signWith(key)
			  .compact();
		return jwt;
	}

	public static String getEmailFromJwtToken(String token) {
		String jwt = token.substring(7);
		Claims claims = Jwts
			  .parser()
			  .setSigningKey(key)
			  .build()
			  .parseClaimsJws(jwt)
			  .getBody();

		return String.valueOf(claims.get("email"));
	}

	private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> auth = new HashSet<>();
		for (GrantedAuthority authority : authorities) {
			auth.add(authority.getAuthority());
		}
		return String.join(",", auth);
	}
}
