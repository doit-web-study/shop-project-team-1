package doit.shop.controller.user.dto;

public record UserModifyRequest(
        String nickname,
        String phoneNumber
) {
}
