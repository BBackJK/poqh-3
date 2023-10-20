package bback.module.poqh3.exceptions;

public class DeveloperMistakeException extends RuntimeException {

    public DeveloperMistakeException() {
        super("개발 오류입니다. 이슈를 남겨주세요.");
    }
}
