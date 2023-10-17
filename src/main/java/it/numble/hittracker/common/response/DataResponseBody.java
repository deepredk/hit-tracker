package it.numble.hittracker.common.response;

import lombok.Getter;

@Getter
public class DataResponseBody<T> extends BaseResponseBody {

    private T data;

    public DataResponseBody(Status status, String errorMessage, T data) {
        super(status, errorMessage);
        this.data = data;
    }

    public static <T> DataResponseBody<T> ok(T data) {
        return new DataResponseBody(Status.SUCCESS, null, data);
    }
}
