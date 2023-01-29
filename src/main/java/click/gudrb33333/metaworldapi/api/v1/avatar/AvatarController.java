package click.gudrb33333.metaworldapi.api.v1.avatar;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.util.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"2. 아바타 API"})
@RequestMapping("/api/v1/avatars")
public class AvatarController {

  private final AvatarService avatarService;
  private final SessionUtil sessionUtil;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "아바타를 생성한다.",
      notes =
          """
            avatarUrl의 URL에서 GLB파일 InputStream으로 받아 S3로 업로드 합니다.<br><br>
    
            publicType이 private인 경우 자기 자신만 사용가능합니다.<br><br>
            
            가능한 Type은 아래 Schema를 클릭하여 Enum Array를 확인해주세요.
          """)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Successful creation."),
        @ApiResponse(code = 400, message = "Invalid avatar supplied."),
        @ApiResponse(code = 403, message = "No permission.")
      })
  public ResponseEntity<String> create(@Valid @RequestBody AvatarCreateDto avatarCreateDto)
      throws IOException {
    Member sessionMember = sessionUtil.getCurrentMember();
    avatarService.createAvatar(avatarCreateDto, sessionMember);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{uuid}")
  @ApiOperation(value = "UUID로 아바타를 조회한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful operation."),
        @ApiResponse(code = 400, message = "Invalid UUID supplied."),
        @ApiResponse(code = 403, message = "No permission."),
        @ApiResponse(code = 404, message = "Avatar not found.")
      })
  public ResponseEntity<AvatarResponseDto> findOne(
      @ApiParam(value = "아바타를 조회하는데 필요한 UUID", required = true)
          @PathVariable("uuid")
          UUID uuid) {
    return ResponseEntity.ok().body(avatarService.findOneAvatar(uuid));
  }
}
