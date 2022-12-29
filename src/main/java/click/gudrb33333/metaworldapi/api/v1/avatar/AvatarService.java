package click.gudrb33333.metaworldapi.api.v1.avatar;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.MemberAssetRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AvatarService {

  private final AvatarRepository avatarRepository;
  private final MemberAssetRepository memberAssetRepository;
  private final SessionUtil sessionUtil;
  private final AwsS3Util awsS3Util;

  @Transactional(rollbackFor = Exception.class)
  public void createAvatar(AvatarCreateDto avatarCreateDto, MultipartFile avatarFile) {

    AssetType assetType = AssetType.AVATAR;
    UUID fileUuid = UUID.randomUUID();
    String extension = StringUtils.getFilenameExtension(avatarFile.getOriginalFilename());
    S3DirectoryType s3DirectoryType = S3DirectoryType.AVATAR;
    GenderType genderType = avatarCreateDto.getGenderType();
    PublicType publicType = avatarCreateDto.getPublicType();

    Avatar avatar =
        Avatar.builder()
            .assetType(assetType)
            .s3AssetUUID(fileUuid)
            .extension(extension)
            .s3DirectoryType(s3DirectoryType)
            .genderType(genderType)
            .publicType(publicType)
            .build();

    Avatar avatarResult = avatarRepository.save(avatar);

    awsS3Util.uploadFileToS3(fileUuid, extension, s3DirectoryType, avatarFile);

    Member currentMember = sessionUtil.getCurrentMember();
    MemberAsset memberAsset =
        MemberAsset.builder().asset(avatarResult).member(currentMember).build();

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
