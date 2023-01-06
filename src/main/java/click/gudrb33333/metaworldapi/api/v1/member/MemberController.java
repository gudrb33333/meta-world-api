package click.gudrb33333.metaworldapi.api.v1.member;

import click.gudrb33333.metaworldapi.api.v1.member.dto.MemberResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Api(tags = {"회원관리 API"})
public class MemberController {

  private final SessionUtil sessionUtil;

  @GetMapping("/me")
  @ApiOperation(value = "로그인된 멤버 조회 ")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Successful operation."),
          @ApiResponse(code = 400, message = "Invalid UUID supplied."),
          @ApiResponse(code = 404, message = "Member not found.")
      })
  public ResponseEntity<MemberResponseDto> findSigninMember() {
    Member member = sessionUtil.getCurrentMember();

    if(member == null){
      throw new CatchedException(ErrorMessage.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
    }

    MemberResponseDto memberResponseDto =
        MemberResponseDto.builder().email(member.getEmail()).build();

    return ResponseEntity.ok().body(memberResponseDto);
  }
}
