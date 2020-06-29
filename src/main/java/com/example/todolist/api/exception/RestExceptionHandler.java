package com.example.todolist.api.exception;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.web.reactive.function.server.ServerRequest;

@ControllerAdvice(annotations = {Api.class})
public class RestExceptionHandler {

  @Autowired
  private ErrorAttributes errorAttributes;

  @ExceptionHandler({ IllegalStateException.class })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, ServerRequest request, Model model) {
        return handleException(HttpStatus.CONFLICT, request, model);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, ServerRequest request, Model model) {
    return handleException(HttpStatus.BAD_REQUEST, request, model);
  }

  @ExceptionHandler({ NoSuchElementException.class })
  protected ResponseEntity<Object> handleNotFound(RuntimeException ex, ServerRequest request, Model model) {
    return handleException(HttpStatus.NOT_FOUND, request, model);
  }

  protected ResponseEntity<Object> handleException(HttpStatus status, ServerRequest request, Model model) {
    model.addAttribute("javax.servlet.error.status_code", status.value());
    Map<String, Object> errorAttributeMap = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(Include.STACK_TRACE));
    return new ResponseEntity<>(errorAttributeMap, new HttpHeaders(), status);
  }
}
