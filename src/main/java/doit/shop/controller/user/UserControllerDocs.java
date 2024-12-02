package doit.shop.controller.user;

import doit.shop.controller.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "유저 관련 API")
@SecurityRequirement(name = "BearerAuth") // Swagger에서 JWT 인증 요구
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 실패")
    ResponseEntity<?> register(
            @Schema(description = "회원가입 요청 데이터", implementation = UserRegisterRequest.class)
            UserRegisterRequest userRegisterRequest
    );

    @Operation(summary = "아이디 중복 확인", description = "회원가입 시 아이디 중복을 확인합니다.")
    @ApiResponse(responseCode = "200", description = "아이디 사용 가능")
    @ApiResponse(responseCode = "400", description = "아이디가 중복되었거나 유효하지 않음")
    ResponseEntity<?> checkDuplicateId(
            @Schema(description = "중복 확인할 아이디", example = "testId")
            String id
    );

    @Operation(summary = "로그인", description = "유저 로그인을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "로그인 실패")
    ResponseEntity<UserLoginResponse> login(
            @Schema(description = "로그인 요청 데이터", implementation = UserLoginRequest.class)
            UserLoginRequest userLoginRequest
    );

    @Operation(summary = "유저 정보 조회", description = "현재 로그인한 유저의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    ResponseEntity<UserInfoResponse> getMyProfile();

    @Operation(summary = "유저 정보 수정", description = "현재 로그인한 유저의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 실패")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    ResponseEntity<?> updateMyProfile(
            @Schema(description = "수정할 유저 정보", implementation = UserUpdateRequest.class)
            UserUpdateRequest updateRequest
    );

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 유저의 계정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    ResponseEntity<?> deleteMyProfile();

    @Operation(summary = "액세스 토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 갱신합니다.")
    @ApiResponse(responseCode = "200", description = "액세스 토큰 갱신 성공")
    @ApiResponse(responseCode = "400", description = "리프레시 토큰이 유효하지 않거나 만료됨")
    ResponseEntity<UserLoginResponse> refreshAccessToken(
            @Schema(description = "리프레시 토큰", example = "refresh-token")
            UserRefreshTokenRequest refreshTokenRequest
    );
}
