package doit.shop.controller.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountIdResponse (
        @Schema(description = "계좌 식별 ID", example = "1")
        Long accountId
){
}
