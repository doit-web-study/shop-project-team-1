package doit.shop.port.jwt.dto;

import lombok.Data;

@Data
public class BasicResDto {
    private String message;

    public BasicResDto(String message) {
        this.message = message;
    }
}
