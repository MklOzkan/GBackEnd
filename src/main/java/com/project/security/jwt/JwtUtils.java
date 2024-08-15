package com.project.security.jwt;

import com.project.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

//JWT token generate edip, JWT token valide etmek için bu classı oluşturduk

@Component
public class JwtUtils {

    private static final Logger LOGGER= LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecretKey;

    @Value("${backendapi.app.jwtExpirationMs}")
    private Long jwtExpirationTime;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

        return generateJwtTokenFromUserName(userDetails.getUsername());
    }



    public String generateJwtTokenFromUserName(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS512 , jwtSecretKey)
                .compact();
    }


    public boolean validateJwtToken(String jwtToken){
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Jwt token is expired: {}" , e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Jwt token is unsupported: {}" , e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Jwt token is invalid: {}" , e.getMessage());
        } catch (SignatureException e) {
            LOGGER.error("Jwt token signature is invalid: {}" , e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Jwt token is empty: {}" , e.getMessage());
        }
        return false;
    }



    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
