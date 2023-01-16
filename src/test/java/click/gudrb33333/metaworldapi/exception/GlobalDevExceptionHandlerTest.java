package click.gudrb33333.metaworldapi.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class GlobalDevExceptionHandlerTest {

  @InjectMocks GlobalDevExceptionHandler globalDevExceptionHandler;

  @Test
  void handleUnAuthenticationException() {
    Exception testException = new Exception("test unauthenticated exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleUnAuthenticationException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("test unauthenticated exception error occurred");
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
            globalDevExceptionHandler.handleConflictException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("test dataIntegrity violation exception error occurred");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getStatusCodeValue()).isEqualTo(409);
  }

  @Test
  void handleSQLException() {
    SQLException testException = new SQLException("test internal server error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleSQLException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("test internal server error occurred");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void handleParseException() {
    IOException testException = new IOException("test io exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleParseException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("test io exception error occurred");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleHashException() {
    NoSuchAlgorithmException testException =
        new NoSuchAlgorithmException("test no such algorithm exception error occurred");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleHashException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("encrypt/decrypt key is requested");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    assertThat(response.getStatusCodeValue()).isEqualTo(423);
  }

  @Test
  void handleAnyException() {
    Exception testException = new Exception("internal server error");

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleAnyException(testException);

    assertThat(Objects.requireNonNull(response.getBody()).getMessage())
        .isEqualTo("internal server error");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void errorResponse() {
    HttpStatus testStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    Throwable testThrowable = null;

    ResponseEntity<ExceptionMessage> response =
        globalDevExceptionHandler.errorResponse(testThrowable, testStatus);

    assertThat(response.getBody()).isEqualTo(null);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }
}
