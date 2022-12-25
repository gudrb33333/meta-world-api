package click.gudrb33333.metaworldapi.api.v1.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateDto {

  @NotBlank
  @Email
  @ApiModelProperty(position=1,value="유저 이메일")
  private String email;

  @NotBlank
  @ApiModelProperty(position=2,value="유저 패스워드")
  private String password;

  @Builder
  public MemberCreateDto(String email, String password){
    this.email = email;
    this.password = password;
  }
}
