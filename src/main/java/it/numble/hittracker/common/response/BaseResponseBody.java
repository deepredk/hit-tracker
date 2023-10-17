package it.numble.hittracker.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BaseResponseBody {

    private Status status;
    private String errorMessage;

    public BaseResponseBody(Status status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static BaseResponseBody ok() {
        return new BaseResponseBody(Status.SUCCESS, null);
    }

    public static BaseResponseBody error(String errorMessage) {
        return new BaseResponseBody(Status.ERROR, errorMessage);
    }
}

enum Status {

    SUCCESS, ERROR
}