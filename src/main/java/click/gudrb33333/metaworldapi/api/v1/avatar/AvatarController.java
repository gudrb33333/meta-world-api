package click.gudrb33333.metaworldapi.api.v1.avatar;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"아바타 API"})
@RequestMapping("/api/v1/avatars")
@RestController
@RequiredArgsConstructor
public class AvatarController {

  private final AvatarService avatarService;

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody AvatarCreateDto avatarCreateDto) throws IOException {
    avatarService.createAvatar(avatarCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<AvatarResponseDto> findOne(@PathVariable("uuid") UUID uuid) {
    return ResponseEntity.ok().body(avatarService.findOneAvatar(uuid));
  }
}
