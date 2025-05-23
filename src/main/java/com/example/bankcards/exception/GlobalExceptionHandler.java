package com.example.bankcards.exception;

import com.example.bankcards.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex){
        log.error("Caught BadCredentialsException: {}", ex);
        var error =  ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        log.error("Caught MethodArgumentNotValidException: {}", ex);
        var errorMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        var errorResponse = ErrorResponse.builder()
                .fieldErrors(errorMap)
                .build();
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }


    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(AlreadyExistsException ex){
        log.error("Caught AlreadyExists: {}", ex);
        var error =  ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationDeniedException ex){
        log.error("Caught AuthorizationDeniedExceotion: {}", ex);
        var error = ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex){
        log.error("Caught MethodArgumentTypeMismatchException: {}", ex);
        var error = ErrorResponse.builder()
                .error("Invalid value for parameter: " + ex.getName())
                .build();
        return ResponseEntity.badRequest()
                .body(error);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex){
        log.error("Caught EntityNotFound: {}", ex);
        var error = ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MoneyTransferException.class)
    public ResponseEntity<ErrorResponse> handleMoneyTransfer(MoneyTransferException ex){
        log.error("Caught MoneyTransferException: {}", ex);
        var error = ErrorResponse.builder()
                .error("Error occurred transferring money: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex){
        log.error("Caught MissingServletRequestParameterException: {}", ex);
        var error = ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(AlreadyProcessedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyProcessed(AlreadyProcessedException ex){
        log.error("Caught AlreadyProcessedException: {}", ex);
        var error = ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(CardDeleteException.class)
    public ResponseEntity<ErrorResponse> handleCardBlock(CardDeleteException ex){
        log.error("Caught CardBlockException: {}", ex);
        var error = ErrorResponse.builder()
                .error("You cannot delete this card, because of: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
        log.error("Caught HttpMessageNotReadable: {}", ex);
        var error = ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("Caught general Exception: {}", ex);
        var error =  ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}
