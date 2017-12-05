package nl.cyberworkz.roboflightmonitor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nl.cyberworkz.roboflightmonitor.exceptions.ErrorResponse;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LogManager.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Throwable.class)
    public @ResponseBody ErrorResponse handleException(HttpServletRequest request, Exception e) {
        LOG.info("Unable to process request for URI({}) with ContentType({})", request.getRequestURI(), request.getContentType(), e);
        return new ErrorResponse(String.format("Error occurred. Try again later or contact support: %s", ExceptionUtils.getStackTrace(e)));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody ErrorResponse handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody ErrorResponse handleBadRequestException(HttpServletRequest request, BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

}
