package doit.shop.port.jwt;

// TEST는 아직 안해본 로직
public interface JwtPort {
    JwtResDto generateTokenPair(String username);
    String refreshAccessToken(String refreshToken);
    String extractEmail(String token);
    boolean isValidateToken(String token, String email);
    boolean isTokenExpired(String token);
    String extractRawToken(String token);
}
