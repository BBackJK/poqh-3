package bback.module.poqh3.exceptions;

public class TableIsOnlyAcceptEntityException extends RuntimeException {

    public TableIsOnlyAcceptEntityException() {
        super("테이블은 오직 Entity Class 만 가능합니다.");
    }
}
