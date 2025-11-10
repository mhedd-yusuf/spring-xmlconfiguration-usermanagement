package com.usermanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFoundException(UserNotFoundException ex,
                                              HttpServletRequest request) {
        logger.error("User not found: {}", ex.getMessage());

        if (isRestRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            body.put("path", request.getRequestURI());

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        } else {
            ModelAndView mav = new ModelAndView("user-form");
            mav.addObject("errors", ex.getMessage());
            return mav;
        }
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request) {
        logger.error("Internal server error: ", ex);

        if (isRestRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", "An unexpected error occurred");
            body.put("path", request.getRequestURI());

            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorMessage", "An unexpected error occurred");
            mav.addObject("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return mav;
        }
    }

    private boolean isRestRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String requestURI = request.getRequestURI();
        return (accept != null && accept.contains("application/json")) ||
                requestURI.startsWith("/api/");
    }
}
