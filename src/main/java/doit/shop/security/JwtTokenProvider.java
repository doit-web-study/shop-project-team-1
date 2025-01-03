//package doit.shop.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    private final long validityInMilliseconds = 3600000;
//
//    //JWT 생성
//    public String createToken(String username) {
//        Claims claims = Jwts.claims().setSubject(username);
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // JWT 유효성 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//}
