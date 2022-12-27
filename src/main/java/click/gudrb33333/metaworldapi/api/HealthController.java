package click.gudrb33333.metaworldapi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Api(tags = {"헬스 체크"})
public class HealthController {
  @ApiOperation(value = "Health Check", notes="AWS 로드밸런스에서 ec2 서버 헬스체크를 한다.")
  @GetMapping
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok().body("OK");
  }
}
