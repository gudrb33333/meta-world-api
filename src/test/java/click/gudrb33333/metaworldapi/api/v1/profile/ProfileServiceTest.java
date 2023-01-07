package click.gudrb33333.metaworldapi.api.v1.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.Profile;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import click.gudrb33333.metaworldapi.repository.ProfileRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;
import org.jets3t.service.CloudFrontServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

  @InjectMocks private ProfileService profileService;

  @Mock private ProfileRepository profileRepository;

  @Mock private AvatarRepository avatarRepository;

  @Mock private MemberRepository memberRepository;

  @Mock private AwsS3Util awsS3Util;

  @Nested
  class createProfile {

    ProfileCreateDto testProfileCreateDto = ProfileCreateDto.builder().nickname("testName").build();

    Member testMember = Member.builder().email("test@test.com").build();

    Profile testProfile = Profile.builder().nickname("testName").build();

    Avatar testAvatar =
        Avatar.builder()
            .id(UUID.randomUUID())
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(UUID.randomUUID())
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(GenderType.MALE)
            .publicType(PublicType.PRIVATE)
            .build();

    @Test
    void whenCurrentMemberHaveProfile() {
      testMember.changeProfile(testProfile);

      assertThatThrownBy(
              () -> {
                profileService.createProfile(testProfileCreateDto, testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.CONFLICT_PROFILE);
    }

    @Test
    void whenMemberDoseNotHaveAvatar() {
      assertThatThrownBy(
              () -> {
                profileService.createProfile(testProfileCreateDto, testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_AVATAR);
    }

    @Test
    void whenMemberHaveAvatar() {
      Optional<Avatar> optionalTestAvatar = Optional.of(testAvatar);
      given(avatarRepository.findOneMemberAvatar(testMember)).willReturn(optionalTestAvatar);

      testProfile.changeAvatar(testAvatar);

      Profile testProfileWithAvatar =
          Profile.builder().nickname(testProfileCreateDto.getNickname()).avatar(testAvatar).build();

      given(profileRepository.save(any(Profile.class))).willReturn(testProfileWithAvatar);
      given(memberRepository.save(any(Member.class))).willReturn(testMember);

      profileService.createProfile(testProfileCreateDto, testMember);
    }
  }

  @Nested
  class findSigninMemberProfile {

    Member testMember = Member.builder().email("test@test.com").build();
    Profile testProfile = Profile.builder().nickname("testName").build();
    Avatar testAvatar =
        Avatar.builder()
            .id(UUID.randomUUID())
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(UUID.randomUUID())
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(GenderType.MALE)
            .publicType(PublicType.PRIVATE)
            .build();

    @Test
    void whenMemberDoseNotHaveProfile() {
      assertThatThrownBy(
              () -> {
                profileService.findSigninMemberProfile(testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_PROFILE);
    }

    @Test
    void whenProfileFound() throws IOException, ParseException, CloudFrontServiceException {
      testProfile.changeAvatar(testAvatar);
      testMember.changeProfile(testProfile);

      given(memberRepository.findMemberWithProfile(testMember)).willReturn(Optional.of(testMember));
      given(awsS3Util.createSignedUrl(S3DirectoryType.AVATAR, testAvatar.getS3AssetUUID(), 3600))
          .willReturn("testSignedAvatarUrl");

      ProfileResponseDto profileResponseDto = profileService.findSigninMemberProfile(testMember);

      assertThat(testProfile.getNickname()).isEqualTo(profileResponseDto.getNickname());
      assertThat("testSignedAvatarUrl").isEqualTo(profileResponseDto.getSignedAvatarUrl());
    }
  }

  @Nested
  class updateSigninMemberProfile {

    ProfileUpdateDto testProfileUpdateDto = ProfileUpdateDto.builder().nickname("testName").build();

    Member testMember = Member.builder().email("test@test.com").build();
    Profile testProfile = Profile.builder().nickname("testName").build();

    Avatar testAvatar =
        Avatar.builder()
            .id(UUID.randomUUID())
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(UUID.randomUUID())
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(GenderType.MALE)
            .publicType(PublicType.PRIVATE)
            .build();

    @Test
    void whenMemberDoseNotHaveProfile() {
      assertThatThrownBy(
              () -> {
                profileService.updateSigninMemberProfile(testProfileUpdateDto, testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_PROFILE);
    }

    @Test
    void whenAvatarNotFound() {
      testMember.changeProfile(testProfile);

      given(memberRepository.findMemberWithProfile(testMember)).willReturn(Optional.of(testMember));

      assertThatThrownBy(
              () -> {
                profileService.updateSigninMemberProfile(testProfileUpdateDto, testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_AVATAR);
    }

    @Test
    void whenMemberHaveProfileAndAvatarFound() {
      Profile testSavedProfile =
          Profile.builder()
              .id(UUID.randomUUID())
              .nickname(testProfileUpdateDto.getNickname())
              .avatar(testAvatar)
              .build();

      testMember.changeProfile(testProfile);

      given(memberRepository.findMemberWithProfile(testMember)).willReturn(Optional.of(testMember));
      given(avatarRepository.findOneMemberAvatar(testMember)).willReturn(Optional.of(testAvatar));
      given(profileRepository.save(testProfile)).willReturn(testSavedProfile);

      profileService.updateSigninMemberProfile(testProfileUpdateDto, testMember);

      assertThat(testSavedProfile.getAvatar()).isEqualTo(testAvatar);
      assertThat(testSavedProfile.getNickname()).isEqualTo(testProfileUpdateDto.getNickname());
    }
  }

  @Nested
  class deleteSigninMemberProfile {

    Member testMember = Member.builder().email("test@test.com").build();
    Profile testProfile = Profile.builder().nickname("testName").build();

    @Test
    void whenMemberDoseNotHaveProfile() {
      assertThatThrownBy(
              () -> {
                profileService.deleteSigninMemberProfile(testMember);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_PROFILE);
    }

    @Test
    void whenProfileFound() {
      testMember.changeProfile(testProfile);

      Member testSavedMember = Member.builder().id(UUID.randomUUID()).build();
      testSavedMember.changeProfile(null);

      Profile testSavedProfile = Profile.builder().id(UUID.randomUUID()).build();
      testSavedProfile.changeDeleteTimeToNow();

      given(memberRepository.findMemberWithProfile(testMember)).willReturn(Optional.of(testMember));

      given(profileRepository.save(testProfile)).willReturn(testSavedProfile);
      given(memberRepository.save(testMember)).willReturn(testSavedMember);

      profileService.deleteSigninMemberProfile(testMember);

      assertThat(testSavedProfile.getDeletedAt()).isNotNull();
      assertThat(testSavedMember.getProfile()).isNull();
    }
  }
}
