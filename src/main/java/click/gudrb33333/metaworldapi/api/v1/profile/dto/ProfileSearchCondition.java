package click.gudrb33333.metaworldapi.api.v1.profile.dto;

import click.gudrb33333.metaworldapi.util.PageUtil;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileSearchCondition extends PageUtil {

  @ApiModelProperty(value = "닉네임")
  private String nickname;

  @ApiModelProperty(value = """
      정렬 조건\s<br>
      (<br>
      profile_inserted_at_asc,<br>
      profile_inserted_at_dsc,<br>
      avatar_inserted_at_asc,<br>
      avatar_inserted_at_dsc,<br>
      )""")
  private List<String> profileOrders;

  @Builder
  public ProfileSearchCondition(String nickname, int page, int size, List<String> profileOrders) {
    super(page, size);
    this.nickname = nickname;
    this.profileOrders = profileOrders;
  }
}
