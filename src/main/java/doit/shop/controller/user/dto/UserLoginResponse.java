package doit.shop.controller.user.dto;

public record UserLoginResponse(
        String accessToken,
        String refreshToken
) {
}
