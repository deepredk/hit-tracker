package it.numble.hittracker.controller.response;

import it.numble.hittracker.controller.response.dto.Empty;
import it.numble.hittracker.controller.response.dto.StatusCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Response<T> {

    private StatusCode statusCode;
    private T detail;
    private String errorMessage;

    public Response(StatusCode statusCode, T detail, String errorMessage) {
        this.statusCode = statusCode;
        this.detail = detail;
        this.errorMessage = errorMessage;
    }

    public static Response<Empty> ok() {
        return new Response<>(StatusCode.SUCCESS, null, null);
    }

    public static <T> Response<T> okWithDetail(T detail) {
        return new Response<T>(StatusCode.SUCCESS, detail, null);
    }

    public static Response<Empty> error(String errorMessage) {
        return new Response<Empty>(StatusCode.ERROR, null, errorMessage);
    }
}
