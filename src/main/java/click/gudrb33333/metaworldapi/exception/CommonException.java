package click.gudrb33333.metaworldapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException {
  private final String message;
  private final HttpStatus status;

  public CommonException(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
