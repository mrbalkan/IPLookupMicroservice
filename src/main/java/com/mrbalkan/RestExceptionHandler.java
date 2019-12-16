package com.mrbalkan;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	//CATCHALL
	@ExceptionHandler({Exception.class, RuntimeException.class})
	@ResponseStatus(value= HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<ApiError>  genericException(Exception ex, WebRequest req) {
		ApiError error = new ApiError();
		error.setTimestamp(LocalDateTime.now());
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
	}

}