package click.gudrb33333.metaworldapi.api.v1.avatar;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.avatar.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.MemberAssetRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvatarService {

  private final AvatarRepository avatarRepository;
  private final MemberAssetRepository memberAssetRepository;
  private final AwsS3Util awsS3Util;

  @Transactional(rollbackFor = Exception.class)
  public void createAvatar(AvatarCreateDto avatarCreateDto, Member sessionMember)
      throws IOException {

    UUID fileUuid = UUID.randomUUID();
    String avatarUrl = avatarCreateDto.getAvatarUrl();

    Avatar avatar =
        Avatar.builder()
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(fileUuid)
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(avatarCreateDto.getGenderType())
            .publicType(avatarCreateDto.getPublicType())
            .build();

    Avatar savedAvatar = avatarRepository.save(avatar);

    awsS3Util.uploadUrlFileToS3(fileUuid, ExtensionType.GLB, S3DirectoryType.AVATAR, avatarUrl);

    MemberAsset memberAsset =
        MemberAsset.builder().asset(savedAvatar).member(sessionMember).build();

    memberAssetRepository.save(memberAsset);
  }

  public AvatarResponseDto findOneAvatar(UUID uuid) {
    Avatar findAvatar =
        avatarRepository
            .findById(uuid)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_AVATAR, HttpStatus.NOT_FOUND);
                });

    return AvatarResponseDto.builder()
        .assetId(findAvatar.getId())
        .assetType(findAvatar.getAssetType())
        .extension(findAvatar.getExtension())
        .publicType(findAvatar.getPublicType())
        .s3AssetUUID(findAvatar.getS3AssetUUID())
        .s3DirectoryType(findAvatar.getS3DirectoryType())
        .genderType(findAvatar.getGenderType())
        .build();
  }
}
