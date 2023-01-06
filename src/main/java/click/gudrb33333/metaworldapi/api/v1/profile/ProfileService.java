package click.gudrb33333.metaworldapi.api.v1.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.Profile;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.MemberAssetRepository;
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import click.gudrb33333.metaworldapi.repository.ProfileRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;
  private final AvatarRepository avatarRepository;
  private final MemberRepository memberRepository;
  private final AwsS3Util awsS3Util;

  public void createProfile(ProfileCreateDto profileCreateDto, Member member) {
    if (member.getProfile() != null) {
      throw new CatchedException(ErrorMessage.CONFLICT_PROFILE, HttpStatus.CONFLICT);
    }

    Avatar avatar =
        avatarRepository
            .findOneMemberAvatar(member)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_AVATAR, HttpStatus.NOT_FOUND);
                });

    Profile profile =
        Profile.builder().nickname(profileCreateDto.getNickname()).avatar(avatar).build();

    Profile savedProfile = profileRepository.save(profile);

    member.changeProfile(savedProfile);

    memberRepository.save(member);
  }

  public ProfileResponseDto findSigninMemberProfile(Member member)
      throws IOException, ParseException, CloudFrontServiceException {
    Profile memberProfile = member.getProfile();

    if (memberProfile == null) {
      throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
    }

    memberProfile =
        profileRepository
            .findById(memberProfile.getId())
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
                });

    String signedAvatarUrl =
        awsS3Util.createSignedUrl(
            S3DirectoryType.AVATAR, memberProfile.getAvatar().getS3AssetUUID(), 3600);

    return ProfileResponseDto.builder()
        .nickname(memberProfile.getNickname())
        .signedAvatarUrl(signedAvatarUrl)
        .build();
  }

  public void updateSigninMemberProfile(ProfileUpdateDto profileUpdateDto, Member member) {
    Profile memberProfile = member.getProfile();

    if (memberProfile == null) {
      throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
    }

    memberProfile =
        profileRepository
            .findById(memberProfile.getId())
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
                });

    Avatar avatar =
        avatarRepository
            .findOneMemberAvatar(member)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_AVATAR, HttpStatus.NOT_FOUND);
                });

    memberProfile.changeAvatarAndNickname(avatar, profileUpdateDto.getNickname());
    profileRepository.save(memberProfile);
  }
}
