package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Api(tags = {"5. 회원인증 API"})
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "회원가입을 한다.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 201, message = "Successful creation."),
          @ApiResponse(code = 400, message = "Invalid request supplied."),
      })
  public ResponseEntity<Object> signup(@Valid @RequestBody MemberCreateDto memberCreateDto) {
    authService.signup(memberCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin")
  @ApiOperation(value = "로그인을 한다.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 201, message = "Successful sign in."),
          @ApiResponse(code = 403, message = "Invalid email or password provided."),
          @ApiResponse(code = 400, message = "Invalid request supplied."),
      })
  public void signin(@Valid @RequestBody MemberLoginDto MemberLoginDto) {
    // use spring security - (WebSecurityConfig,AuthService(loadUserByUsername))
  }

  @DeleteMapping(value="/logout")
  @ApiOperation(value = "로그아웃을 한다.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Successful logout."),
          @ApiResponse(code = 400, message = "Invalid request supplied."),
      })
  public ResponseEntity<Object> logout(@ApiIgnore HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok().body("OK");
  }
}
