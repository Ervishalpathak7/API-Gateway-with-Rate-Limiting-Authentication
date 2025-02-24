package ApiGateway.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    public String handleInternalServer(InternalServerException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequest(BadRequestException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbidden(ForbiddenException e) {
        return e.getMessage();
    }
}
