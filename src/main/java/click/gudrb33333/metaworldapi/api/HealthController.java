package click.gudrb33333.metaworldapi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"헬스 체크 API"})
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "헬스 체크를 한다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful operation.")})
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok().body("OK");
  }
}
