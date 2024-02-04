package com.jumia.FA.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 7008375124389347049L;
    @Value("${secret}")
    private String jwtSecret;
    public static final long TOKEN_VALIDITY = 10 * 60 * 60;

    public String generateJwtToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256,jwtSecret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUserNameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    public String getUserNameFromToken(String token){
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
