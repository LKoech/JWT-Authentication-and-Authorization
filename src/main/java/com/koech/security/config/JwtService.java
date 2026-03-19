package com.koech.security.config;

import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "7A9F2E8D4C6B1A3E5F8D9C2A7B4E6F1D3A8C5E9F2B7D4A6C8E1F5A9D3C7B2F8E4A";

    public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

	private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
	}
}
