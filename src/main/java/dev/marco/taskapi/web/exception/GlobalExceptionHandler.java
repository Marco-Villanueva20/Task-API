package dev.marco.taskapi.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.marco.taskapi.domain.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
		ProblemDetail problemDetails = ProblemDetail.forStatus(404);
		problemDetails.setTitle("Resource Not Found");
		problemDetails.setDetail(ex.getMessage());
		return problemDetails;
	}
	
	
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGenericException(Exception ex) {
	    ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    problemDetails.setTitle("Internal Server Error");
	    problemDetails.setDetail("An unexpected error occurred");
	    return problemDetails;
	}

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetails = ProblemDetail.forStatus(400);
		problemDetails.setTitle("Validation Error");
		Map<String, Object> errors = new HashMap<>();
		for (FieldError error: ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		problemDetails.setProperty("errors", errors);
		return problemDetails;
	}

}
