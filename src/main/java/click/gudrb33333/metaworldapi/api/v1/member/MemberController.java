package click.gudrb33333.metaworldapi.api.v1.member;

import click.gudrb33333.metaworldapi.api.v1.member.dto.MemberResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Api(tags = {"4. 회원 API"})
public class MemberController {

  private final SessionUtil sessionUtil;

  @GetMapping("/me")
  @ApiOperation(value = "로그인된 멤버를 조회한다.", notes = "세션정보로 자신을 조회합니다.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Successful operation."),
          @ApiResponse(code = 400, message = "Invalid request supplied."),
          @ApiResponse(code = 401, message = "Not sign in.")
      })
  public ResponseEntity<MemberResponseDto> findSigninMember() {
    Member member = sessionUtil.getCurrentMember();

    if(member == null){
      throw new EntityNotFoundException(ErrorMessage.NOT_FOUND_MEMBER);
    }

    MemberResponseDto memberResponseDto =
        MemberResponseDto.builder().email(member.getEmail()).build();

    return ResponseEntity.ok().body(memberResponseDto);
  }
}
