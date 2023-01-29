package click.gudrb33333.metaworldapi.api.v1.clothing;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = {"3. 의상 API"})
@RequestMapping("/api/v1/clothing")
public class ClothingController {

  private final ClothingService clothingService;
  private final SessionUtil sessionUtil;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "의상을 생성한다.",
      notes =
      """
        로컬파일을 InputStream으로 받아 S3로 업로드 합니다.<br><br>
      
        associateLink는 참고 사이트 URL입니다.<br><br>
        
        publicType이 private인 경우 자기 자신만 사용가능합니다.<br><br>
        
        가능한 Type은 아래 Schema를 클릭하여 Enum Array를 확인해주세요.
      """)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "successful creation."),
        @ApiResponse(code = 400, message = "Invalid clothing supplied."),
        @ApiResponse(code = 403, message = "no permission.")
      })
  public ResponseEntity<String> create(
      @Valid @RequestPart ClothingCreateDto clothingCreateDto,
      @RequestPart MultipartFile multipartFile) {
    Member sessionMember = sessionUtil.getCurrentMember();
    clothingService.createClothing(clothingCreateDto, multipartFile, sessionMember);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{uuid}")
  @ApiOperation(value = "UUID로 의상을 조회한다.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful operation."),
        @ApiResponse(code = 400, message = "Invalid UUID supplied."),
        @ApiResponse(code = 403, message = "No permission."),
        @ApiResponse(code = 404, message = "Clothing not found.")
      })
  public ResponseEntity<ClothingResponseDto> findOne(
      @ApiParam(value = "의상을 조회하는데 필요한 UUID", required = true)
          @PathVariable("uuid")
          UUID uuid)
      throws IOException, ParseException, CloudFrontServiceException {
    Member sessionMember = sessionUtil.getCurrentMember();
    return ResponseEntity.ok().body(clothingService.findOneClothing(uuid, sessionMember));
  }
}
