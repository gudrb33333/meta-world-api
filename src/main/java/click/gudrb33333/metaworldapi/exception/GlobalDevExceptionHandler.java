package click.gudrb33333.metaworldapi.exception;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import java.text.ParseException;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Profile("dev")
public class GlobalDevExceptionHandler {

  protected Logger logger;

  public ResponseEntity<?> handleUnAuthenticationException(Exception e) {
    return errorResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      AccessDeniedException.class
  })
  public ResponseEntity<?> handleAccessDeniedExceptionException(AccessDeniedException e) {
    return errorResponse(e, HttpStatus.FORBIDDEN);
  }


  @ExceptionHandler({
      EntityNotFoundException.class
  })
  public ResponseEntity<?> handleNotFoundException(EntityNotFoundException e) {
    return errorResponse(e, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler({
    DataIntegrityViolationException.class,
    SQLIntegrityConstraintViolationException.class
  })
  public ResponseEntity<?> handleConflictException(Exception e) {
    return errorResponse(e, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({SQLException.class, DataAccessException.class, RuntimeException.class})
  public ResponseEntity<?> handleSQLException(Exception e) {
    return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({
    IOException.class,
    ParseException.class,
    JsonParseException.class,
    JsonMappingException.class
  })
  public ResponseEntity<?> handleParseException(Exception e) {
    return errorResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidKeyException.class, NoSuchAlgorithmException.class})
  public ResponseEntity<?> handleHashException(Exception e) {
    return errorResponse(new Exception("Encrypt/Decrypt key is requested"), HttpStatus.LOCKED);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<?> handleAnyException(Exception e) {
    return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({CommonException.class})
  public ResponseEntity<?> handleCommonException(CommonException e) {
    return errorResponse(e, e.getStatus());
  }

  protected ResponseEntity<ExceptionMessage> errorResponse(Throwable throwable, HttpStatus status) {
    if (null != throwable) {
      throwable.printStackTrace();
      return response(new ExceptionMessage(throwable), status);
    } else {
      return response(null, status);
    }
  }

  protected <T> ResponseEntity<T> response(T body, HttpStatus status) {
    return new ResponseEntity<T>(body, new HttpHeaders(), status);
  }
}
