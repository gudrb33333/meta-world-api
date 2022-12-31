package click.gudrb33333.metaworldapi.api.v1.avatar.dto;

import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvatarResponseDto {

  private UUID assetId;
  private AssetType assetType;
  private UUID s3AssetUUID;
  private ExtensionType extension;
  private S3DirectoryType s3DirectoryType;
  private PublicType publicTyp;
  private GenderType genderType;

  @Builder
  public AvatarResponseDto(
      UUID assetId,
      AssetType assetType,
      UUID s3AssetUUID,
      ExtensionType extension,
      S3DirectoryType s3DirectoryType,
      PublicType publicType,
      GenderType genderType) {
    this.assetId = assetId;
    this.assetType = assetType;
    this.s3AssetUUID = s3AssetUUID;
    this.extension = extension;
    this.s3DirectoryType = s3DirectoryType;
    this.publicTyp = publicType;
    this.genderType = genderType;
  }
}
