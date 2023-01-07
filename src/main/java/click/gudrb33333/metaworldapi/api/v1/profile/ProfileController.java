package click.gudrb33333.metaworldapi.api.v1.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.text.ParseException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Api(tags = {"프로필 API"})
@RequestMapping("/api/v1/profiles")
@RestController("meta-world-api ProfileController")
public class ProfileController {

  private final ProfileService profileService;
  private final SessionUtil sessionUtil;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "프로필을 생성한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "successful creation."),
        @ApiResponse(code = 400, message = "Invalid profile supplied."),
        @ApiResponse(code = 403, message = "no permission.")
      })
  public ResponseEntity<Object> create(@Valid @RequestBody ProfileCreateDto profileCreateDto) {
    Member member = sessionUtil.getCurrentMember();
    profileService.createProfile(profileCreateDto, member);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/me")
  @ApiOperation(value = "로그인된 멤버의 프로필을 조회한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful operation."),
        @ApiResponse(code = 403, message = "No permission."),
        @ApiResponse(code = 404, message = "profile not found.")
      })
  public ResponseEntity<ProfileResponseDto> findSigninMemberProfile()
      throws IOException, ParseException, CloudFrontServiceException {
    Member member = sessionUtil.getCurrentMember();
    return ResponseEntity.ok().body(profileService.findSigninMemberProfile(member));
  }

  @PatchMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "로그인된 멤버의 프로필을 수정한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Successful operation."),
        @ApiResponse(code = 400, message = "Invalid profile supplied."),
        @ApiResponse(code = 403, message = "No permission."),
        @ApiResponse(code = 404, message = "profile not found.")
      })
  public ResponseEntity<String> updateSigninMemberProfile(
      @Valid @RequestBody ProfileUpdateDto profileUpdateDto) {
    Member member = sessionUtil.getCurrentMember();
    profileService.updateSigninMemberProfile(profileUpdateDto, member);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "로그인된 멤버의 프로필을 삭제한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Successful operation."),
        @ApiResponse(code = 403, message = "No permission."),
        @ApiResponse(code = 404, message = "profile not found.")
      })
  public ResponseEntity<String> deleteSigninMemberProfile() {
    Member member = sessionUtil.getCurrentMember();
    profileService.deleteSigninMemberProfile(member);
    return ResponseEntity.noContent().build();
  }
}
