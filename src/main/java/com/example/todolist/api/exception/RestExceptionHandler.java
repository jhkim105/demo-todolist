package com.example.todolist.api.exception;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice(annotations = {Api.class})
public class RestExceptionHandler {

  @Autowired
  private ErrorAttributes errorAttributes;

  @ExceptionHandler({ IllegalStateException.class })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleException(HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
    return handleException(HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({ NoSuchElementException.class })
  protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
    return handleException(HttpStatus.NOT_FOUND, request);
  }

  protected ResponseEntity<Object> handleException(HttpStatus status, WebRequest request) {
    request.setAttribute("javax.servlet.error.status_code", status.value(), WebRequest.SCOPE_REQUEST);
    Map<String, Object> errorAttributeMap = errorAttributes.getErrorAttributes(request, false);
    return new ResponseEntity<>(errorAttributeMap, new HttpHeaders(), status);
  }
}
