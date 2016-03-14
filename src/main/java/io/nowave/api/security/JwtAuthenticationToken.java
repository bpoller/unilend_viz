package io.nowave.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.time.ZoneId;
import java.util.Date;

import static io.nowave.api.WebSecurityConfig.SIGNING_KEY;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private Claims claims;

    public JwtAuthenticationToken(String tokenString) {
        super(null);
        claims = Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(tokenString).getBody();
    }

    public boolean hasExpired(){
        Date now = from(now().atZone(systemDefault()).toInstant());
        return claims.getExpiration().before(now);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject();
    }
}