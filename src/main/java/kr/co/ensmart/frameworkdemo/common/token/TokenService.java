package kr.co.ensmart.frameworkdemo.common.token;

import java.util.Date;

import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenService {
	private final long minuteMillis = 60L * 1000L;
	
	private final String issuer = "hmg.hyundai.com.cn";
	private final String subject = "hmg api token";

    private SecretKeyProvider secretKeyProvider = new SecretKeyProvider();
    
    public String generateToken() {
    	return generateToken( minuteMillis );
    }
    
    public String generateToken(long expirationOffsetMillis) {
    	final long now = System.currentTimeMillis();
    	
		return Jwts.builder()
			    .setSubject(subject)
			    .setIssuer(issuer)
			    .setIssuedAt(new Date(now))
			    .setNotBefore(new Date(now - minuteMillis))
			    .setExpiration(new Date(now + expirationOffsetMillis))
			    .signWith(secretKeyProvider.getSecretKey())
			    .compact();
    }

    public boolean verifyToken(String token) {
    	try {
    		final Jws<Claims> jws = parseToken(token);
    		
    		final String tokenSubject = jws.getBody().getSubject();
    		final String tokenIssuer = jws.getBody().getIssuer();
    		
    		if ( StringUtils.hasText(tokenSubject) && StringUtils.hasText(tokenIssuer) ) {
    			return tokenSubject.equals(this.subject) && tokenIssuer.equals(this.issuer);
    		}
		} catch (Exception e) {
			log.error("[COMMON][TOKEN_SERVICE] invalidate token: " + token, e);
		}
    	
        return false;
    }
    
    public Jws<Claims> parseToken(String token) {
    	return Jwts.parserBuilder()
    			.setSigningKey(secretKeyProvider.getSecretKey())
    			.build()
    			.parseClaimsJws(token);
    }
}
