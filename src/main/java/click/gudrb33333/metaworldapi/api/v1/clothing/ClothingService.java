package click.gudrb33333.metaworldapi.api.v1.clothing;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingResponseDto;
import click.gudrb33333.metaworldapi.entity.Clothing;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CommonException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.clothing.ClothingRepository;
import click.gudrb33333.metaworldapi.repository.memberasset.MemberAssetRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClothingService {

  private final ClothingRepository clothingRepository;
  private final MemberAssetRepository memberAssetRepository;
  private final AwsS3Util awsS3Util;

  @Transactional(rollbackFor = Exception.class)
  public void createClothing(
      ClothingCreateDto clothingCreateDto, MultipartFile multipartFile, Member sessionMember) {
    UUID fileUuid = UUID.randomUUID();

    Clothing clothing =
        Clothing.builder()
            .assetType(AssetType.CLOTHING)
            .s3AssetUUID(fileUuid)
            .price(clothingCreateDto.getPrice())
            .associateLink(clothingCreateDto.getAssociateLink())
            .brand(clothingCreateDto.getBrand())
            .extension(ExtensionType.GLB)
            .genderType(clothingCreateDto.getGenderType())
            .detailDescription(clothingCreateDto.getDetailDescription())
            .publicType(clothingCreateDto.getPublicType())
            .name(clothingCreateDto.getName())
            .s3DirectoryType(S3DirectoryType.CLOTHING)
            .serialNumber(clothingCreateDto.getSerialNumber())
            .build();

    Clothing savedClothing = clothingRepository.save(clothing);

    awsS3Util.uploadLocalFileToS3(
        fileUuid, ExtensionType.GLB, S3DirectoryType.CLOTHING, multipartFile);

    MemberAsset memberAsset =
        MemberAsset.builder().member(sessionMember).asset(savedClothing).build();

    memberAssetRepository.save(memberAsset);
  }

  public ClothingResponseDto findOneClothing(UUID uuid, Member sessionMember)
      throws IOException, ParseException, CloudFrontServiceException {
    Clothing clothing =
        clothingRepository
            .findById(uuid)
            .orElseThrow(
                () -> {
                  throw new CommonException(ErrorMessage.NOT_FOUND_CLOTHING, HttpStatus.NOT_FOUND);
                });

    if (clothing.getPublicType() != PublicType.PUBLIC) {
      memberAssetRepository
          .findByMemberAndAsset(sessionMember, clothing)
          .orElseThrow(
              () -> {
                throw new CommonException(HttpStatus.FORBIDDEN.toString(), HttpStatus.FORBIDDEN);
              });
    }

    String signedUrl = awsS3Util.createSignedUrl(S3DirectoryType.CLOTHING, clothing.getS3AssetUUID(), 86400);

    return ClothingResponseDto.builder()
        .id(clothing.getId())
        .brand(clothing.getBrand())
        .detailDescription(clothing.getDetailDescription())
        .name(clothing.getName())
        .signedClothingUrl(signedUrl)
        .price(clothing.getPrice())
        .serialNumber(clothing.getSerialNumber())
        .associateLink(clothing.getAssociateLink())
        .genderType(clothing.getGenderType())
        .build();
  }
}
