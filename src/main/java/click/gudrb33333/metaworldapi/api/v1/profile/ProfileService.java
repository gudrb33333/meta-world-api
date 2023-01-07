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
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import click.gudrb33333.metaworldapi.repository.ProfileRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    Member memberWithProfile =
        memberRepository
            .findMemberWithProfile(member)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
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
                  throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
                });

    Profile profile = memberWithProfile.getProfile();

    Avatar avatar =
        avatarRepository
            .findOneMemberAvatar(member)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_AVATAR, HttpStatus.NOT_FOUND);
                });

    profile.changeAvatarAndNickname(avatar, profileUpdateDto.getNickname());
    profileRepository.save(profile);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteSigninMemberProfile(Member member) {
    Member memberWithProfile =
        memberRepository
            .findMemberWithProfile(member)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_PROFILE, HttpStatus.NOT_FOUND);
                });

    Profile profile = memberWithProfile.getProfile();

    memberWithProfile.changeProfile(null);
    profile.changeDeleteTimeToNow();

    profileRepository.save(profile);
    memberRepository.save(memberWithProfile);
  }
}
