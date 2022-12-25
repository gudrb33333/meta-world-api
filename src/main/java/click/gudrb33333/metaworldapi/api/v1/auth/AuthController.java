package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Api(tags = {"회원인증 및 회원관리 Api"})
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @ApiOperation(value = "유저 회원가입", notes = "유저 회원가입을 한다.")
  public ResponseEntity<Object> signup(@Valid @RequestBody MemberCreateDto memberCreateDto)
      throws Exception {
    authService.signup(memberCreateDto);
    return ResponseEntity.ok().body("OK");
  }
}
