//package doit.shop.common.jwt;
//
//import doit.shop.exception.CustomException;
//import doit.shop.exception.ErrorCode;
//import io.jsonwebtoken.Jwts;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//@RequiredArgsConstructor
//public class TokenService {
//
//    private final JwtProvider jwtProvider;
//
//    public String regenerateAccessToken(String refreshToken){
//        if(jwtProvider.isExpired(refreshToken)){
//            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
//        }
//
//        long now = (new Date()).getTime();
//
//        return Jwts.builder()
//                .claim("sub", "accessToken")
//                .claim("auth", "USER")
//                .claim("exp", new Date(now+3600000))
//                .claim("aud", jwtProvider.getUserId(refreshToken))
//                .claim("iat", now)
//                .signWith(jwtProvider.key)
//                .compact();
//    }
//}
