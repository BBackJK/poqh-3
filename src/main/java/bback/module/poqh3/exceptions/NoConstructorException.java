package bback.module.poqh3.exceptions;

public class NoConstructorException extends RuntimeException {

    public NoConstructorException() {
        super("생성자가 존재하지 않습니다.");
    }

    public NoConstructorException(String msg) {
        super(msg);
    }
}
