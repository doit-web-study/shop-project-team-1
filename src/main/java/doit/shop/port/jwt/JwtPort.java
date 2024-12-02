package doit.shop.port.jwt;

public interface JwtPort {
    JwtResDto generateTokenPair(String username);
    String refreshAccessToken(String refreshToken);
    String extractEmail(String token);
    boolean isValidateToken(String token, String email);
    boolean isTokenExpired(String token);
    String extractRawToken(String token);
}
