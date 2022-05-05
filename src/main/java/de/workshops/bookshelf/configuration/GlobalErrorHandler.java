package de.workshops.bookshelf.configuration;

import de.workshops.bookshelf.book.BookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookException.class)
    public ResponseEntity<String> handleException(Exception exception, HttpServletRequest request) {
        log.debug(request.getRequestURI() + ": " + exception.getMessage());

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }
}

