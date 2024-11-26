package doit.shop.global.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRequestDto (
        @Schema(description = "access")
        String AccessToken,

        @Schema(description = "refresh")
        String RefreshToken
){
}
