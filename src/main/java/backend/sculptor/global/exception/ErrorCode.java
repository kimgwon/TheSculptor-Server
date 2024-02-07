package backend.sculptor.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다"),
    STONE_NOT_FOUND("해당 돌을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND("해당 방명록을 찾을 수 없습니다"),

    STONE_NOT_COMPLETE("박물관에서 목표일이 지나지 않은 돌은 조회할 수 없습니다");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}