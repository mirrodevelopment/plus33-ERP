/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : GlobalExceptionHandler.java
 * Path              : src/main/java/com/plus33/erp/common/exception/GlobalExceptionHandler.java
 * Purpose           : Intercepts all uncaught exceptions thrown by any controller in the
 *                     ERP and translates them into consistent JSON ErrorResponse payloads
 *                     with appropriate HTTP status codes.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * @RestControllerAdvice class providing centralized exception handling for all
 * REST controllers in the PLUS33 Coffee ERP. Each @ExceptionHandler maps a
 * specific exception type to an HTTP status and ErrorResponse body.
 *
 * Exception handlers:
 *   ResourceNotFoundException  → 404 Not Found
 *     Thrown when a queried entity does not exist in the database.
 *
 *   DuplicateResourceException → 409 Conflict
 *     Thrown when a create/update would violate uniqueness constraints.
 *
 *   BusinessException          → 400 Bad Request
 *     Thrown for domain rule violations (e.g. insufficient stock, invalid state).
 *
 *   MethodArgumentNotValidException → 400 Bad Request
 *     Thrown by Spring MVC when @Valid/@Validated DTO validation fails.
 *     Collects all field-level errors into a Map<fieldName, message> included
 *     in ErrorResponse.fieldErrors.
 *
 *   ConstraintViolationException → 400 Bad Request
 *     Thrown when Jakarta Bean Validation on method parameters fails.
 *
 *   HttpMessageNotReadableException → 400 Bad Request
 *     Thrown when the JSON request body is malformed or unreadable.
 *
 *   DataIntegrityViolationException → 409 Conflict
 *     Thrown when a DB constraint (FK, unique) violation occurs at persist time.
 *
 *   ObjectOptimisticLockingFailureException / OptimisticLockException
 *     → 409 Conflict. Thrown during concurrent purchase order modifications.
 *
 *   AccessDeniedException → 403 Forbidden
 *     Thrown when @PreAuthorize evaluation fails for an authenticated user.
 *
 *   Exception (catch-all) → 500 Internal Server Error
 *     Captures all unhandled runtime exceptions with the exception message.
 *
 * All responses use ErrorResponse DTO with: statusCode, error, message,
 * path, fieldErrors (nullable), and timestamp.
 ******************************************************************************/
package com.plus33.erp.common.exception;

import com.plus33.erp.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code GlobalExceptionHandler}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.exception}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to GlobalExceptionHandlerService.</p>
 *
 * <p><b>Module Deps      :</b> Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the resource not found event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the duplicate resource event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the business event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the method argument not valid event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> fieldErrors = new HashMap<>();
        String message = "Validation failed";
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
            if ("Validation failed".equals(message) && error.getDefaultMessage() != null) {
                message = error.getDefaultMessage();
            }
        }

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the constraint violation event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            fieldErrors.put(propertyPath, violation.getMessage());
        });

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Constraint violation",
                request.getRequestURI(),
                fieldErrors,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the http message not readable event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Malformed JSON request body",
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the data integrity violation event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Database constraint violation",
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the access denied event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler({
            org.springframework.orm.ObjectOptimisticLockingFailureException.class,
            jakarta.persistence.OptimisticLockException.class
    })
    /**
     * Handles the optimistic locking event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    public ResponseEntity<ErrorResponse> handleOptimisticLocking(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Purchase order was modified by another transaction. Please retry.",
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles the general event or exception in the business workflow.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param ex the ex input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}