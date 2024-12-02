package doit.shop.port.jwt;

import lombok.Data;

@Data
public class JwtResDto {
    private final String accessToken;
    private final String refreshToken;
}
