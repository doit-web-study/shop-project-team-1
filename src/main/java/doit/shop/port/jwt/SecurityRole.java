package doit.shop.port.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = """
        \tSELLER: 판매자
        \tUSER: 유저
        """)
@Getter
public enum SecurityRole {
    SELLER("판매자", "ROLE_SELLER", "Seller"),
    USER("유저", "ROLE_USER", "User");

    private final String displayName;
    private final String key;
    private final String title;

    SecurityRole(String displayName, String key, String title) {
        this.displayName = displayName;
        this.key = key;
        this.title = title;
    }
}
