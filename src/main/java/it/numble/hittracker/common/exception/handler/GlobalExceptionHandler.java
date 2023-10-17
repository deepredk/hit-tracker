package it.numble.hittracker.common.exception.handler;

import it.numble.hittracker.common.exception.BaseException;
import it.numble.hittracker.common.response.BaseResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final String field = "${field}";

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseBody handleBaseException(BaseException e) {
        return BaseResponseBody.error(e.getMessage());
    }
}
