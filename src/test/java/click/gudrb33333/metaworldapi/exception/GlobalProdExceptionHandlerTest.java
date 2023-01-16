package click.gudrb33333.metaworldapi.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class GlobalProdExceptionHandlerTest {

  @InjectMocks GlobalProdExceptionHandler globalProdExceptionHandler;

  @Test
  void handleUnAuthenticationException() {
    Exception testException = new Exception("test unauthenticated exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleUnAuthenticationException(testException);

    assertThat(String.valueOf(response.getBody()))
        .isEqualTo("{message=test unauthenticated exception error occurred}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleConflictException() {
    DataIntegrityViolationException testException =
        new DataIntegrityViolationException(
            "test dataIntegrity violation exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleConflictException(testException);

    assertThat(String.valueOf(response.getBody()))
        .isEqualTo("{message=test dataIntegrity violation exception error occurred}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getStatusCodeValue()).isEqualTo(409);
  }

  @Test
  void handleSQLException() {
    SQLException testException = new SQLException("test internal server error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleSQLException(testException);

    assertThat(String.valueOf(response.getBody()))
        .isEqualTo("{message=test internal server error occurred}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void handleParseException() {
    IOException testException = new IOException("test io exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleParseException(testException);

    assertThat(String.valueOf(response.getBody()))
        .isEqualTo("{message=test io exception error occurred}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleHashException() {
    NoSuchAlgorithmException testException =
        new NoSuchAlgorithmException("test no such algorithm exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleHashException(testException);

    assertThat(String.valueOf(response.getBody()))
        .isEqualTo("{message=encrypt/decrypt key is requested}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    assertThat(response.getStatusCodeValue()).isEqualTo(423);
  }

  @Test
  void handleAnyException() {
    Exception testException = new Exception("internal server error");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleAnyException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("{message=internal server error}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void errorResponse() {
    HttpStatus testStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String testStr = "internal server error";

    ResponseEntity<Object> response = globalProdExceptionHandler.errorResponse(testStr, testStatus);

    assertThat(String.valueOf(response.getBody())).isEqualTo("{message=internal server error}");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }
}
