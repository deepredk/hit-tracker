package it.numble.hittracker.common.exception.handler;

import it.numble.hittracker.controller.response.Response;
import it.numble.hittracker.controller.response.dto.Empty;
import it.numble.hittracker.common.exception.BaseException;
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
    public Response<Empty> handleBaseException(BaseException e) {
        return Response.error(e.getMessage());
    }
}
