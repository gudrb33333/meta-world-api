package click.gudrb33333.metaworldapi.api.v1.avatar.dto;

import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.GenderTypeConverter;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Convert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvatarCreateDto {

  @ApiModelProperty(value = "성별", allowableValues = "male,female")
  private GenderType genderType;

  @ApiModelProperty(value = "공개 여부", allowableValues = "public,private", required = true)
  private PublicType publicType;

  @ApiModelProperty(value = "Ready Player Me 아바타 url", required = true)
  private String avatarUrl;

  @Builder
  public AvatarCreateDto(GenderType genderType, PublicType publicType, String avatarUrl) {
    this.genderType = genderType;
    this.publicType = publicType;
    this.avatarUrl = avatarUrl;
  }
}
