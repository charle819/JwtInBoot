package com.example.JwtInBoot.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.JwtInBoot.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.example.JwtInBoot.util.Constants.SIGNING_KEY;
import static com.example.JwtInBoot.util.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;

@Component
public class JwtTokenUtil implements Serializable {

	public String getUserNameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpiryDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <R> R getClaimFromToken(String token, Function<Claims, R> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(User user) {
		return doGenerateToken(user.getUserName());
	}
	
	public boolean validateToke(String token, UserDetails userDetails){
		final String userName = getUserNameFromToken(token);
		return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
	}
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}
	
	private boolean isTokenExpired(String token){
		final Date expiryDate = getExpiryDateFromToken(token);
		return expiryDate.before(new Date());
	}
	
	private String doGenerateToken(String subject){
		Claims claims = Jwts.claims().setSubject(subject);
		claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
		
		return Jwts.builder()
					.setClaims(claims)
					.setIssuer("http://JwtInBoot.com")
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_VALIDITY_SECONDS*1000))
					.signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
					.compact();
	}
}
