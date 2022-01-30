package com.nace.exceptions.handlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nace.constants.NaceEnum;
import com.nace.dto.ErrorResponse;
import com.nace.exceptions.EntityNotFoundException;

/**
 * Global Exception Handler.
 * 
 * @author Deepak Bhalla
 *
 */
@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles exception of type ConstraintViolationException.
     * 
     * @param ex      - ConstraintViolationException.class
     * @param request - WebRequest
     * @return ResponseEntity of type ErrorResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
            WebRequest request) {
        List<String> details = ex.getConstraintViolations().parallelStream().map(e -> e.getMessage())
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(NaceEnum.Errors.BAD_REQUEST.getValue(), details, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exception of type EntityNotFoundException.
     * 
     * @param ex      - EntityNotFoundException.class
     * @param request - WebRequest
     * @return ResponseEntity of type ErrorResponse
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(NaceEnum.Errors.NO_RECORD_FOUND.getValue(),
                Arrays.asList(NaceEnum.Errors.NO_RECORD_FOUND_MSG.getValue()), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception of type Exception.
     * 
     * @param ex      - Exception.class
     * @param request - WebRequest
     * @return ResponseEntity of type ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleExceptions(Exception ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(NaceEnum.Errors.INTERNAL_SERVER_ERROR.getValue(),
                Arrays.asList(NaceEnum.Errors.INTERNAL_SERVER_ERROR_MSG.getValue()), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exception of type NumberFormatException.
     * 
     * @param ex      - NumberFormatException.class
     * @param request - WebRequest
     * @return ResponseEntity of type ErrorResponse
     */
    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(NaceEnum.Errors.INTERNAL_SERVER_ERROR.getValue(),
                Arrays.asList(NaceEnum.Errors.INTERNAL_SERVER_ERROR_MSG.getValue()), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exception of type IOException.
     * 
     * @param ex      - IOException.class
     * @param request - WebRequest
     * @return ResponseEntity of type ErrorResponse
     */
    @ExceptionHandler(IOException.class)
    public final ResponseEntity<ErrorResponse> handleIOException(IOException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(NaceEnum.Errors.INTERNAL_SERVER_ERROR.getValue(),
                Arrays.asList(NaceEnum.Errors.INTERNAL_SERVER_ERROR_MSG.getValue()), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
