package click.gudrb33333.metaworldapi.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Profile("prod")
public class GlobalProdExceptionHandler {

  protected Logger logger;

  public ResponseEntity<?> handleUnAuthenticationException(Exception e) {
    return errorResponse("400 UNAUTHENTICATED", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    DataIntegrityViolationException.class,
    SQLIntegrityConstraintViolationException.class
  })
  public ResponseEntity<?> handleConflictException(Exception e) {
    return errorResponse("409 CONFLICT", HttpStatus.CONFLICT);
  }

  @ExceptionHandler({SQLException.class, DataAccessException.class, RuntimeException.class})
  public ResponseEntity<?> handleSQLException(Exception e) {
    return errorResponse("500 INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({
    IOException.class,
    ParseException.class,
    JsonParseException.class,
    JsonMappingException.class
  })
  public ResponseEntity<?> handleParseException(Exception e) {
    return errorResponse("400 BAD REQUEST", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidKeyException.class, NoSuchAlgorithmException.class})
  public ResponseEntity<?> handleHashException(Exception e) {
    return errorResponse("423 LOCKED", HttpStatus.LOCKED);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<?> handleAnyException(Exception e) {
    return errorResponse("500 INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({CommonException.class})
  public ResponseEntity<?> handleCommonException(CommonException e) {
    HttpStatus status = e.getStatus();
    return errorResponse(String.valueOf(status), status);
  }

  protected ResponseEntity<String> errorResponse(String message, HttpStatus status) {
    return new ResponseEntity<String>(message, new HttpHeaders(), status);
  }
}
