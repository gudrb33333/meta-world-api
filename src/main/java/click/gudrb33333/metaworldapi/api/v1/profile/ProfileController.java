package click.gudrb33333.metaworldapi.api.v1.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.text.ParseException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Api(tags = {"프로필 API"})
@RequestMapping("/api/v1/profiles")
@RestController("meta-world-api ProfileController")
public class ProfileController {

  private final ProfileService profileService;
  private final SessionUtil sessionUtil;

  @ApiOperation(value = "유저 프로필 생성", notes = "유저 프로필을 생성한다.")
  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody ProfileCreateDto profileCreateDto) {
    Member member = sessionUtil.getCurrentMember();
    profileService.createProfile(profileCreateDto, member);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/me")
  public ResponseEntity<ProfileResponseDto> findSigninMemberProfile()
      throws IOException, ParseException, CloudFrontServiceException {
    Member member = sessionUtil.getCurrentMember();
    return ResponseEntity.ok().body(profileService.findSigninMemberProfile(member));
  }
}
