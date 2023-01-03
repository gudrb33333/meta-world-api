package click.gudrb33333.metaworldapi.api.v1.clothing.dto;

import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothingCreateDto {

  @ApiModelProperty(position = 1, value = "이름")
  private String name;

  @ApiModelProperty(position = 2, value = "브랜드")
  private String brand;

  @ApiModelProperty(position = 3, value = "품번")
  private String serialNumber;

  @ApiModelProperty(position = 4, value = "성별", allowableValues = "male,female")
  private GenderType genderType;

  @ApiModelProperty(position = 5, value = "가격")
  @PositiveOrZero
  private int price;

  @ApiModelProperty(position = 6, value = "관련 링크")
  private String associateLink;

  @ApiModelProperty(position = 7, value = "상세 설명")
  private String detailDescription;

  @ApiModelProperty(position = 8, value = "공개 여부", allowableValues = "public,private")
  private PublicType publicType;

  @Builder
  public ClothingCreateDto(
      String name,
      String brand,
      String serialNumber,
      GenderType genderType,
      int price,
      String associateLink,
      String detailDescription,
      PublicType publicType) {
    this.name = name;
    this.brand = brand;
    this.serialNumber = serialNumber;
    this.genderType = genderType;
    this.price = price;
    this.associateLink = associateLink;
    this.detailDescription = detailDescription;
    this.publicType = publicType;
  }
}
