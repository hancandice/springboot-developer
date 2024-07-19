package me.jeehahn.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration duration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .setSubject(user.getEmail())
            .claim("id", user.getId())
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
            .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Consider logging the exception for debugging purposes
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        org.springframework.security.core.userdetails.User principal =
            new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", Long.class);
    }

    public Date getExpirationDate(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody();
    }
}
