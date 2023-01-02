package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Api(tags = {"회원인증 API"})
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @ApiOperation(value = "유저 회원가입", notes = "유저 회원가입을 한다.")
  public ResponseEntity<Object> signup(@Valid @RequestBody MemberCreateDto memberCreateDto) {
    authService.signup(memberCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin")
  @ApiOperation(value = "유저 로그인", notes = "로그인을 한다.")
  public void signin(@Valid @RequestBody MemberLoginDto MemberLoginDto) {
    // use spring security - (WebSecurityConfig,AuthService(loadUserByUsername))
  }

  @DeleteMapping(value="/logout")
  @ApiOperation(value = "유저 로그아웃", notes = "로그아웃을 한다.")
  public ResponseEntity<Object> logout(@ApiIgnore HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok().body("OK");
  }
}
