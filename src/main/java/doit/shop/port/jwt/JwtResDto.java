package doit.shop.port.jwt;

import lombok.Data;

// TEST는 아직 안해본 로직
@Data
public class JwtResDto {
    private final String accessToken;
    private final String refreshToken;
}
