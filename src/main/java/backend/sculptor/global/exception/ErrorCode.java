package backend.sculptor.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다"),
    STONE_NOT_FOUND("해당 돌을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND("해당 방명록을 찾을 수 없습니다"),
    PRODUCT_NOT_FOUND("해당 상품을 찾을 수 없습니다."),

    STONE_NOT_COMPLETE("박물관에서 목표일이 지나지 않은 돌은 조회할 수 없습니다"),
    NOT_USER_STONE("해당 유저의 돌이 아닙니다"),
    STONE_ALREADY_PURCHASE("해당 돌이 이미 구매한 상품입니다."),
    STONE_NOT_PURCHASE_ITEM("해당 돌이 구매하지 않은 상품입니다."),

    USER_POWDER_NOT_ENOUGH("해당 유저의 돌가루가 부족합니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}