package doit.shop.controller.account;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.AccountIdResponse;
import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Account", description = "계좌 관리 API")
public interface AccountControllerDocs {

    @Operation(summary = "신규 계좌 등록", description = "신규 계좌를 등록한다.")
    @ApiResponse(responseCode = "200", description = "신규 계좌 등록 성공")
    @ApiResponse(responseCode = "400", description = "신규 계좌 등록 실패")
    AccountIdResponse registerAccount(
            @Schema(description = "계좌 등록 정보", implementation = AccountRegisterRequest.class)
            AccountRegisterRequest accountRegisterRequest
    );

    @Operation(summary = "본인 계좌 리스트 정보 조회", description = "본인 계좌 정보를 리스트의 형태로 조회한다.")
    @ApiResponse(responseCode = "200", description = "계좌 정보 조회 성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "계좌 정보 조회 실패")
    ListWrapper<AccountInfoResponse> getAccountList();

    @Operation(summary = "계좌 단건 정보 조회", description = "계좌 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "계좌 정보 조회 성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "계좌 정보 조회 실패")
    AccountInfoResponse getAccountInfo(
            @Schema(description = "계좌 식별 ID", example = "1")
            Long accountId
    );

    @Operation(summary = "계좌 정보 수정", description = "계좌 정보를 수정한다.")
    @ApiResponse(responseCode = "200", description = "계좌 정보 수정 성공")
    @ApiResponse(responseCode = "400", description = "계좌 정보 수정 실패")
    AccountInfoResponse updateAccountInfo(
            @Schema(description = "계좌 식별 ID", example = "1")
            Long accountId,
            @Schema(description = "계좌 정보 수정", implementation = AccountRegisterRequest.class)
            AccountUpdateRequest accountRegisterRequest
    );

    @Operation(summary = "계좌에 입금", description = "계좌에 일정 금액을 입금한다.")
    @ApiResponse(responseCode = "200", description = "계좌 입금 성공")
    @ApiResponse(responseCode = "400", description = "계좌 입금 실패")
    void depositAccount(
            @Schema(description = "계좌 식별 ID", example = "1")
            Long accountId,
            @Schema(description = "입금 금액", example = "10000")
            Integer amount
    );

    @Operation(summary = "계좌에서 출금", description = "계좌에서 일정 금액을 출금한다.")
    @ApiResponse(responseCode = "200", description = "계좌 출금 성공")
    @ApiResponse(responseCode = "400", description = "계좌 출금 실패")
    void withdrawAccount(
            @Schema(description = "계좌 식별 ID", example = "1")
            Long accountId,
            @Schema(description = "출금 금액", example = "10000")
            Integer amount
    );


}
