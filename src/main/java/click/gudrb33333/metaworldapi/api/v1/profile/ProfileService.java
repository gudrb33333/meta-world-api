package click.gudrb33333.metaworldapi.api.v1.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileSearchCondition;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.Profile;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.avatar.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.memberasset.MemberAssetRepository;
import click.gudrb33333.metaworldapi.repository.member.MemberRepository;
import click.gudrb33333.metaworldapi.repository.profile.ProfileRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;
  private final AvatarRepository avatarRepository;
  private final MemberRepository memberRepository;
  private final MemberAssetRepository memberAssetRepository;
  private final AwsS3Util awsS3Util;

  @Transactional(rollbackFor = Exception.class)
  public void createProfile(ProfileCreateDto profileCreateDto, Member member) {
    memberRepository
        .findMemberWithProfile(member)
        .ifPresent(
            (x) -> {
              throw new DataIntegrityViolationException(ErrorMessage.CONFLICT_PROFILE);
            });

    UUID fileUuid = UUID.randomUUID();
    Avatar avatar =
        Avatar.builder()
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(fileUuid)
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(profileCreateDto.getGenderType())
            .publicType(profileCreateDto.getPublicType())
            .build();

    Avatar savedAvatar = avatarRepository.save(avatar);

    awsS3Util.uploadUrlFileToS3(
        fileUuid, ExtensionType.GLB, S3DirectoryType.AVATAR, profileCreateDto.getAvatarUrl());

    MemberAsset memberAsset = MemberAsset.builder().asset(savedAvatar).member(member).build();

    memberAssetRepository.save(memberAsset);

    Profile profile =
        Profile.builder().nickname(profileCreateDto.getNickname()).avatar(savedAvatar).build();

    Profile savedProfile = profileRepository.save(profile);

    member.changeProfile(savedProfile);

    memberRepository.save(member);
  }

  public ProfileResponseDto findSigninMemberProfile(Member member)
      throws IOException, ParseException, CloudFrontServiceException {
    Member memberWithProfile =
        memberRepository
            .findMemberWithProfile(member)
            .orElseThrow(
                () -> {
                  throw new EntityNotFoundException(ErrorMessage.NOT_FOUND_PROFILE);
                });

    Profile profile = memberWithProfile.getProfile();

    String signedAvatarUrl =
        awsS3Util.createSignedUrl(
            S3DirectoryType.AVATAR, profile.getAvatar().getS3AssetUUID(), 3600);

    return ProfileResponseDto.builder()
        .nickname(profile.getNickname())
        .signedAvatarUrl(signedAvatarUrl)
        .build();
  }

  public void updateSigninMemberProfile(ProfileUpdateDto profileUpdateDto, Member member) {
    Member memberWithProfile =
        memberRepository
            .findMemberWithProfile(member)
            .orElseThrow(
                () -> {
                  throw new EntityNotFoundException(ErrorMessage.NOT_FOUND_PROFILE);
                });

    Profile profile = memberWithProfile.getProfile();

    UUID fileUuid = UUID.randomUUID();
    Avatar avatar =
        Avatar.builder()
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(fileUuid)
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(profileUpdateDto.getGenderType())
            .publicType(profileUpdateDto.getPublicType())
            .build();

    Avatar savedAvatar = avatarRepository.save(avatar);

    awsS3Util.uploadUrlFileToS3(
        fileUuid, ExtensionType.GLB, S3DirectoryType.AVATAR, profileUpdateDto.getAvatarUrl());

    MemberAsset memberAsset = MemberAsset.builder().asset(savedAvatar).member(member).build();

    memberAssetRepository.save(memberAsset);

    profile.changeAvatarAndNickname(savedAvatar, profileUpdateDto.getNickname());
    profileRepository.save(profile);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteSigninMemberProfile(Member member) {
    Member memberWithProfile =
        memberRepository
            .findMemberWithProfile(member)
            .orElseThrow(
                () -> {
                  throw new EntityNotFoundException(ErrorMessage.NOT_FOUND_PROFILE);
                });

    Profile profile = memberWithProfile.getProfile();

    memberWithProfile.changeProfile(null);
    profile.changeDeleteTimeToNow();

    profileRepository.save(profile);
    memberRepository.save(memberWithProfile);
  }

  public Page<ProfileResponseDto> findAllWithCondition(
      ProfileSearchCondition condition, Pageable pageable) {
    Page<Profile> profiles = profileRepository.findAllWithCondition(condition, pageable);

    return profiles.map(this::convertProfileResponseDto);
  }

  private ProfileResponseDto convertProfileResponseDto(Profile profile) {
    String signedAvatarUrl;

    try {
      signedAvatarUrl =
          awsS3Util.createSignedUrl(
              S3DirectoryType.AVATAR, profile.getAvatar().getS3AssetUUID(), 3600);
    } catch (IOException | ParseException | CloudFrontServiceException e) {
      throw new IllegalStateException(
          ErrorMessage.AWS_S3_UTIL_IO_ERROR);
    }

    return ProfileResponseDto.builder()
        .nickname(profile.getNickname())
        .signedAvatarUrl(signedAvatarUrl)
        .build();
  }
}
