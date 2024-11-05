package doit.shop.controller.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountRegisterRequest(
        @Schema(description = "계좌 애칭", example = "나의 작고 소중한 계좌")
        String accountName,

        @Schema(description = "계좌 번호", example = "123-456-789")
        String accountNumber,

        @Schema(description = "은행 이름", example = "국민은행")
        String accountBankName
) {
}
