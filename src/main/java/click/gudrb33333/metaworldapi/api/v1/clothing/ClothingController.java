package click.gudrb33333.metaworldapi.api.v1.clothing;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"의상 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clothing")
public class ClothingController {

  private final ClothingService clothingService;
  private final SessionUtil sessionUtil;

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestPart ClothingCreateDto clothingCreateDto,
      @RequestPart MultipartFile multipartFile) {
    Member sessionMember = sessionUtil.getCurrentMember();
    clothingService.createClothing(clothingCreateDto, multipartFile, sessionMember);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<ClothingResponseDto> findOne(@PathVariable("uuid") UUID uuid)
      throws IOException, ParseException, CloudFrontServiceException {
    Member sessionMember = sessionUtil.getCurrentMember();
    return ResponseEntity.ok().body(clothingService.findOneClothing(uuid, sessionMember));
  }
}
