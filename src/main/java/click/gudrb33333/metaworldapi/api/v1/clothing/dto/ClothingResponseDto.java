package click.gudrb33333.metaworldapi.api.v1.clothing.dto;

import click.gudrb33333.metaworldapi.entity.type.GenderType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothingResponseDto {

  private UUID id;

  private String signedClothingUrl;

  private String name;

  private String brand;

  private String serialNumber;

  private GenderType genderType;

  private int price;

  private String associateLink;

  private String detailDescription;

  @Builder
  public ClothingResponseDto(
      UUID id,
      String signedClothingUrl,
      String name,
      String brand,
      String serialNumber,
      GenderType genderType,
      int price,
      String associateLink,
      String detailDescription) {
    this.id = id;
    this.signedClothingUrl = signedClothingUrl;
    this.name = name;
    this.brand = brand;
    this.serialNumber = serialNumber;
    this.genderType = genderType;
    this.price = price;
    this.associateLink = associateLink;
    this.detailDescription = detailDescription;
  }
}
