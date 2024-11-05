package doit.shop.controller.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountUpdateRequest(
        @Schema(description = "계좌 애칭", example = "나의 작고 소중한 계좌")
        String accountName
) {
}
