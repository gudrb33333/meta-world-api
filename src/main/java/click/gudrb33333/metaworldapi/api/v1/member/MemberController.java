package click.gudrb33333.metaworldapi.api.v1.member;

import click.gudrb33333.metaworldapi.api.v1.member.dto.MemberResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
  @ApiOperation(value = "로그인된 유저 조회 ", notes = "로그인된 유저를 조회한다.")
  public ResponseEntity<MemberResponseDto> findSigninMember() {
    Member member = sessionUtil.getCurrentMember();
    MemberResponseDto memberResponseDto =
        MemberResponseDto.builder().email(member.getEmail()).build();

    return ResponseEntity.ok().body(memberResponseDto);
  }
}
