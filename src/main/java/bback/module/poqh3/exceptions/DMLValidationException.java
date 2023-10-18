package bback.module.poqh3.exceptions;

public class DMLValidationException extends RuntimeException {

    public DMLValidationException() {
        this("유효하지 않은 SQL 입니다.");
    }

    public DMLValidationException(String msg) {
        super(msg);
    }
}
