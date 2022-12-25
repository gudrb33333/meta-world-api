package click.gudrb33333.metaworldapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CatchedException extends RuntimeException {
  private String message;
  private HttpStatus status;

  public CatchedException(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }

  public CatchedException(String message) {
    super(message);
    this.message = message;
  }
}
