package doit.shop.common.jwt;

import doit.shop.exception.CustomException;
import doit.shop.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;

@Component
public class JwtProvider {

    private Key key;
    private final RefreshTokenRepository tokenRepository;

    public JwtProvider(@Value("${jwt.secret.key}") String secretKey, RefreshTokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Authentication authentication){
        String authorities = "USER";
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("sub", "accessToken")
                .claim("auth", authorities)
                .claim("exp", new Date(now+3600000))
                .claim("aud", authentication.getName())
                .claim("iat", now)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication){
        String authorities = "USER";
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("sub", "refreshToken")
                .claim("auth", authorities)
                .claim("exp", new Date(now+604800000)) // 7일
                .claim("aud", authentication.getName())
                .claim("iat", now)
                .signWith(key)
                .compact();
    }

    public String regenerateAccessToken(String refreshToken){
        if(isExpired(refreshToken)){
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("sub", "accessToken")
                .claim("auth", "USER")
                .claim("exp", new Date(now+3600000))
                .claim("aud", getUserId(refreshToken))
                .claim("iat", now)
                .signWith(key)
                .compact();
    }

    public void disableToken(String token){
        RefreshToken toDelete = tokenRepository.findByRefreshToken(token);
        tokenRepository.delete(toDelete);
    }

    public Date extractExpirationTime(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload().get("exp", Date.class);
    }

    public Boolean isExpired(String token) {
        try{
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload().get("exp", Date.class).before(new Date());
            return false;
        }catch (ExpiredJwtException e){
            return true;
        }
    }

    public String getUserId(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload();
            return (String) ((LinkedHashSet<?>) claims.get("aud")).iterator().next();
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    public boolean isValidToken(String token, String userId){
        RefreshToken refreshToken = tokenRepository.findByLoginId(userId);
        if(isExpired(refreshToken.getRefreshToken())){
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
        return !isExpired(token) && getUserId(token).equals(userId);
    }

    public String resolveToken(HttpServletRequest request) {
        String token =  request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 쿠키가 없으면 null 반환
    }

    public Authentication getAuthentication(String accessToken) {
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(getUserId(accessToken), "", authorities);
    }


}
