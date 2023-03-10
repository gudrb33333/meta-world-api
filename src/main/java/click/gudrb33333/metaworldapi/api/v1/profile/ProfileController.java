package click.gudrb33333.metaworldapi.api.v1.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileSearchCondition;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Member;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@Api(tags = {"1. 프로필 API"})
@RequestMapping("/api/v1/profiles")
@RestController("meta-world-api ProfileController")
public class ProfileController {

  private final ProfileService profileService;
  private final SessionUtil sessionUtil;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "프로필을 생성한다.",
      notes =
        """
          S3에 아바타 3D 에셋을 업로드 한 후, 프로필을 생성합니다.<br>
          avatarUrl의 URL에서 GLB파일을 InputStream으로 받아 업로드 합니다.<br><br>
        
          publicType이 private인 경우 자기 자신만 사용가능합니다.<br><br>
          가능한 Type은 아래 Schema를 클릭하여 Enum Array를 확인해주세요.
        """)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "successful creation."),
        @ApiResponse(code = 400, message = "Invalid request supplied."),
        @ApiResponse(code = 401, message = "Not sign in."),
      })
  public ResponseEntity<Object> create(@Valid @RequestBody ProfileCreateDto profileCreateDto) {
    Member member = sessionUtil.getCurrentMember();
    profileService.createProfile(profileCreateDto, member);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  @ApiOperation(
      value = "조건에 맞는 프로필을 페이지로 조회한다.",
      notes = "프로필의 닉네임, 아바타 3D 자산(서명된 URL), 페이지 정보가 반환됩니다.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Successful operation."),
          @ApiResponse(code = 401, message = "Not sign in."),
      })
  public Page<ProfileResponseDto> findAllWithCondition(
      @Valid ProfileSearchCondition condition, @ApiIgnore Pageable pageable) {
    return profileService.findAllWithCondition(condition, pageable);
  }

  @GetMapping("/me")
  @ApiOperation(value = "로그인된 멤버의 프로필을 조회한다.", notes = "세션정보로 자신의 프로필을 조회합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful operation."),
        @ApiResponse(code = 401, message = "Not sign in."),
        @ApiResponse(code = 404, message = "Profile not found.")
      })
  public ResponseEntity<ProfileResponseDto> findSigninMemberProfile()
      throws IOException, ParseException, CloudFrontServiceException {
    Member member = sessionUtil.getCurrentMember();
    return ResponseEntity.ok().body(profileService.findSigninMemberProfile(member));
  }

  @PatchMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(
      value = "로그인된 멤버의 프로필을 수정한다.",
      notes =
          """
            S3에 아바타 3D 에셋을 업로드 한 후, 세션정보로 자신의 프로필을 수정합니다.<br>
            avatarUrl의 URL에서 GLB파일을 InputStream으로 받아 업로드 합니다.<br><br>
            
            publicType이 private인 경우 자기 자신만 사용가능합니다.<br><br>
            가능한 Type은 아래 Schema를 클릭하여 Enum Array를 확인해주세요.
          """)
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Successful operation."),
        @ApiResponse(code = 400, message = "Invalid request supplied."),
        @ApiResponse(code = 401, message = "Not sign in."),
        @ApiResponse(code = 404, message = "Profile not found.")
      })
  public ResponseEntity<String> updateSigninMemberProfile(
      @Valid @RequestBody ProfileUpdateDto profileUpdateDto) {
    Member member = sessionUtil.getCurrentMember();
    profileService.updateSigninMemberProfile(profileUpdateDto, member);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "로그인된 멤버의 프로필을 삭제한다.", notes = "세션정보로 자신의 프로필을 소프트 삭제합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Successful operation."),
        @ApiResponse(code = 401, message = "Not sign in."),
        @ApiResponse(code = 404, message = "Profile not found.")
      })
  public ResponseEntity<String> deleteSigninMemberProfile() {
    Member member = sessionUtil.getCurrentMember();
    profileService.deleteSigninMemberProfile(member);
    return ResponseEntity.noContent().build();
  }
}
