package click.gudrb33333.metaworldapi.api.v1.avatar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.MemberAssetRepository;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {

  @InjectMocks private AvatarService avatarService;

  @Mock private AvatarRepository avatarRepository;

  @Mock private MemberAssetRepository memberAssetRepository;

  @Mock private AwsS3Util awsS3Util;

  @Mock private SessionUtil sessionUtil;

  @Test
  void createAvatar() throws IOException {
    Member mockMember =
        Member.builder()
            .email("test@test.com")
            .build();

    AvatarCreateDto mockAvatarCreateDto =
        AvatarCreateDto.builder()
            .avatarUrl("testUrl")
            .publicType(PublicType.PRIVATE)
            .genderType(GenderType.MALE)
            .build();

    UUID mockFileUuid = UUID.randomUUID();
    String mockAvatarUrl = mockAvatarCreateDto.getAvatarUrl();
    AssetType mockAssetType = AssetType.AVATAR;
    ExtensionType mockExtension = ExtensionType.GLB;
    S3DirectoryType mockS3DirectoryType = S3DirectoryType.AVATAR;
    GenderType mockGenderType = mockAvatarCreateDto.getGenderType();
    PublicType mockPublicType = mockAvatarCreateDto.getPublicType();

    Avatar mockAvatar =
        Avatar.builder()
            .assetType(mockAssetType)
            .s3AssetUUID(mockFileUuid)
            .extension(mockExtension)
            .s3DirectoryType(mockS3DirectoryType)
            .genderType(mockGenderType)
            .publicType(mockPublicType)
            .build();

    MemberAsset mockMemberAsset =
        MemberAsset.builder()
            .asset(mockAvatar)
            .member(mockMember)
            .build();

    given(avatarRepository.save(any(Avatar.class))).willReturn(mockAvatar);
    given(sessionUtil.getCurrentMember()).willReturn(mockMember);
    given(memberAssetRepository.save(any(MemberAsset.class))).willReturn(mockMemberAsset);

    avatarService.createAvatar(mockAvatarCreateDto);
  }

  @Nested
  class findOneAvatar {

    @Test
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    void whenNotFoundMember() {
      UUID mockAssetId = UUID.randomUUID();

      given(avatarRepository.findById(mockAssetId)).willReturn(Optional.empty());

      assertThatThrownBy(
              () -> {
                avatarService.findOneAvatar(mockAssetId);
              })
          .isInstanceOf(CatchedException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_AVATAR);
    }

    @Test
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    void whenFoundMember() {
      UUID mockAssetId = UUID.randomUUID();
      Avatar mockAvatar =
          Avatar.builder()
              .id(mockAssetId)
              .assetType(AssetType.AVATAR)
              .extension(ExtensionType.GLB)
              .genderType(GenderType.MALE)
              .publicType(PublicType.PRIVATE)
              .s3AssetUUID(UUID.randomUUID())
              .s3DirectoryType(S3DirectoryType.AVATAR)
              .build();

      given(avatarRepository.findById(mockAssetId)).willReturn(Optional.of(mockAvatar));

      AvatarResponseDto findAvatar = avatarService.findOneAvatar(mockAssetId);

      assertThat(mockAvatar.getId()).isEqualTo(findAvatar.getAssetId());
      assertThat(mockAvatar.getAssetType()).isEqualTo(findAvatar.getAssetType());
      assertThat(mockAvatar.getExtension()).isEqualTo(findAvatar.getExtension());
      assertThat(mockAvatar.getGenderType()).isEqualTo(findAvatar.getGenderType());
      assertThat(mockAvatar.getPublicType()).isEqualTo(findAvatar.getPublicType());
      assertThat(mockAvatar.getS3AssetUUID()).isEqualTo(findAvatar.getS3AssetUUID());
      assertThat(mockAvatar.getS3DirectoryType()).isEqualTo(findAvatar.getS3DirectoryType());
    }
  }
}