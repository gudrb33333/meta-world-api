package click.gudrb33333.metaworldapi.api;

import click.gudrb33333.metaworldapi.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Api(tags = {"AWS 로드밸런스 헬스 체크"})
public class HealthController {
  @ApiOperation(value = "Health Check", notes="AWS 로드밸런스에서 ec2 서버 헬스체크를 한다.")
  @GetMapping
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok().body("OK");
  }
}
