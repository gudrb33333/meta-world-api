package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.AssetType.Values;
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
@Table(name = "avatars")
@DiscriminatorValue(Values.AVATAR)
public class Avatar extends Asset {

  @Column(name = "gender_type")
  @Convert(converter = GenderTypeConverter.class)
  private GenderType genderType;

  @Builder
  public Avatar(
      AssetType assetType,
      UUID s3AssetUUID,
      String extension,
      S3DirectoryType s3DirectoryType,
      PublicType publicType,
      GenderType genderType) {
    super(assetType, s3AssetUUID, extension, s3DirectoryType, publicType);
    this.genderType = genderType;
  }
}
