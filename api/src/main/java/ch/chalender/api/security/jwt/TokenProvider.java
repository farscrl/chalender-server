package ch.chalender.api.security.jwt;

import ch.chalender.api.config.AppProperties;
import ch.chalender.api.dto.UserInfo;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication, UserInfo userInfo) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        Claims claims = Jwts.claims().setSubject(userInfo.getId());
        claims.put("displayName", userInfo.getFirstName() + " " + userInfo.getLastName());
        claims.put("firstName", userInfo.getFirstName());
        claims.put("lastName", userInfo.getLastName());
        claims.put("email", userInfo.getEmail());
        claims.put("roles", userInfo.getRoles());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date()).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
