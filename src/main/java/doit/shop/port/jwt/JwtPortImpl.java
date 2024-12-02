package doit.shop.port.jwt;

import doit.shop.common.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

// TEST는 아직 안해본 로직
@Component
public class JwtPortImpl implements JwtPort{
    private final JwtConfig jwtProperties;

    public JwtPortImpl(JwtConfig jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public JwtResDto generateTokenPair(String username) {
        String jti = UUID.randomUUID().toString();
        Key key = new SecretKeySpec(jwtProperties.getSecret().getBytes(), SignatureAlgorithm.HS512.getJcaName());

        String accessToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(key)
                .setId(jti)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
                .signWith(key)
                .setId(jti)
                .compact();

        return new JwtResDto(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        if (isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        String username = extractEmail(refreshToken);
        return generateAccessToken(username);
    }

    private String generateAccessToken(String username) {
        String jti = UUID.randomUUID().toString();
        Key key = new SecretKeySpec(jwtProperties.getSecret().getBytes(), SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(key)
                .setId(jti)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(extractRawToken(token)).getSubject();
    }

    private String extractJTI(String token) {
        return extractAllClaims(extractRawToken(token)).getId();
    }

    private Date extractExpirationTime(String token) {
        return extractAllClaims(extractRawToken(token)).getExpiration();
    }

    private long getRemainingTime(String token) {
        long currentTimeMillis = Instant.now().toEpochMilli();
        Date expirationTime = extractExpirationTime(token);
        long remainingTimeMillis = expirationTime.getTime() - currentTimeMillis;
        return Math.max(remainingTimeMillis, 0);
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(extractRawToken(token)).getExpiration().before(new Date());
    }

    public String extractRawToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token;
    }
}
