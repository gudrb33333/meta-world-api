package click.gudrb33333.metaworldapi.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalDevExceptionHandlerTest {

  @InjectMocks GlobalDevExceptionHandler globalDevExceptionHandler;

  private final Exception testException = new Exception();

  @Test
  void handleUnAuthenticationException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleUnAuthenticationException(testException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleConflictException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleConflictException(testException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getStatusCodeValue()).isEqualTo(409);
  }

  @Test
  void handleSQLException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleSQLException(testException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void handleParseException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleParseException(testException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleHashException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleHashException(testException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    assertThat(response.getStatusCodeValue()).isEqualTo(423);
  }

  @Test
  void handleAnyException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalDevExceptionHandler.handleAnyException(testException);

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
