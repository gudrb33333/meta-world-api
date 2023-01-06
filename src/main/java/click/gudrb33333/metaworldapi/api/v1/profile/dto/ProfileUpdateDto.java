package click.gudrb33333.metaworldapi.api.v1.profile.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateDto {

  @NotBlank
  private String nickname;

  @Builder
  public ProfileUpdateDto(String nickname) {
    this.nickname = nickname;
  }
}
