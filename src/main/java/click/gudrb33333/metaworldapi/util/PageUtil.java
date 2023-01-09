package click.gudrb33333.metaworldapi.util;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageUtil {

  @Min(0)
  @ApiModelProperty(value = "페이지 번호(0페이지 부터 시작)", required = true)
  private int page;

  @Min(0)
  @Max(100)
  @ApiModelProperty(value = "페이지 크기(0,100)", required = true)
  private int size;

  public PageUtil(Integer page, Integer size) {
    this.page = page;
    this.size = size;
  }
}
