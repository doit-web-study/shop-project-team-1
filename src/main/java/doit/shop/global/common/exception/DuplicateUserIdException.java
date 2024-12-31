package doit.shop.global.common.exception;

public class DuplicateUserIdException extends RuntimeException {
    private final int detailStatusCode;

    public DuplicateUserIdException(String message, int detailStatusCode) {
        super(message);
        this.detailStatusCode = detailStatusCode;
    }

    public int getDetailStatusCode() {
        return detailStatusCode;
    }
}