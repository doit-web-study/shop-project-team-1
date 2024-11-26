package doit.shop.global.auth.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String refreshToken;
    private String accessToken;
    private Long accessTokenExpiresIn;
}
