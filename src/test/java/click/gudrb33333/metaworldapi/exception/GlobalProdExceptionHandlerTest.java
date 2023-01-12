package click.gudrb33333.metaworldapi.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalProdExceptionHandlerTest {

  @InjectMocks GlobalProdExceptionHandler globalProdExceptionHandler;

  private final Exception testException = new Exception();

  @Test
  void handleUnAuthenticationException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleUnAuthenticationException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("400 UNAUTHENTICATED");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleConflictException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleConflictException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("409 CONFLICT");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getStatusCodeValue()).isEqualTo(409);
  }

  @Test
  void handleSQLException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleSQLException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("500 INTERNAL SERVER ERROR");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void handleParseException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleParseException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("400 BAD REQUEST");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getStatusCodeValue()).isEqualTo(400);
  }

  @Test
  void handleHashException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleHashException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("423 LOCKED");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    assertThat(response.getStatusCodeValue()).isEqualTo(423);
  }

  @Test
  void handleAnyException() {
    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleAnyException(testException);

    assertThat(String.valueOf(response.getBody())).isEqualTo("500 INTERNAL SERVER ERROR");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }

  @Test
  void handleCommonException() {
    CommonException testCommonException =
        new CommonException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);

    ResponseEntity<ExceptionMessage> response =
        (ResponseEntity<ExceptionMessage>)
            globalProdExceptionHandler.handleCommonException(testCommonException);

    assertThat(response.getStatusCode()).isEqualTo(testCommonException.getStatus());
    assertThat(response.getStatusCodeValue()).isEqualTo(404);
  }

  @Test
  void errorResponse() {
    HttpStatus testStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String testStr = "Internal Server Error";

    ResponseEntity<String> response =
        globalProdExceptionHandler.errorResponse(testStr, testStatus);

    assertThat(response.getBody()).isEqualTo("Internal Server Error");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getStatusCodeValue()).isEqualTo(500);
  }
}
