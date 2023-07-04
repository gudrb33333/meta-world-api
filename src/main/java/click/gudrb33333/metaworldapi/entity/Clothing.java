package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.AssetType.Values;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.GenderTypeConverter;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "clothing")
@DiscriminatorValue(Values.CLOTHING)
public class Clothing extends Asset {

  private String name;

  private String brand;

  private String serialNumber;

  @Convert(converter = GenderTypeConverter.class)
  private GenderType genderType;

  private int price;

  private String associateLink;

  @Column(columnDefinition = "TEXT")
  private String detailDescription;

  @Builder
  public Clothing(
      UUID id,
      AssetType assetType,
      UUID s3AssetUUID,
      ExtensionType extension,
      S3DirectoryType s3DirectoryType,
      PublicType publicType,
      String name,
      String brand,
      String serialNumber,
      GenderType genderType,
      int price,
      String associateLink,
      String detailDescription
      ) {
    super(id, assetType, s3AssetUUID, extension, s3DirectoryType, publicType);
    this.name = name;
    this.brand = brand;
    this.serialNumber = serialNumber;
    this.genderType = genderType;
    this.price = price;
    this.associateLink = associateLink;
    this.detailDescription = detailDescription;
  }
}
