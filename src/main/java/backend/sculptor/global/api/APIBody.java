package backend.sculptor.global.api;

import lombok.Getter;

@Getter
public class APIBody<T>{
    private final int code;
    private final String message;
    private final T data;

    public APIBody(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> APIBody<T> of(int code, String message, T data) {
        return new APIBody<>(code, message, data);
    }


}
