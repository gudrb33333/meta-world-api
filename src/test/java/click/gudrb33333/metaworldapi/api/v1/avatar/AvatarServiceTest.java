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
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.avatar.AvatarRepository;
import click.gudrb33333.metaworldapi.repository.memberasset.MemberAssetRepository;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import click.gudrb33333.metaworldapi.util.SessionUtil;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
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
    Member testMember =
        Member.builder()
            .email("test@test.com")
            .build();

    AvatarCreateDto testAvatarCreateDto =
        AvatarCreateDto.builder()
            .avatarUrl("testUrl")
            .publicType(PublicType.PRIVATE)
            .genderType(GenderType.MALE)
            .build();

    UUID testFileUuid = UUID.randomUUID();
    String testAvatarUrl = testAvatarCreateDto.getAvatarUrl();
    AssetType testAssetType = AssetType.AVATAR;
    ExtensionType testExtension = ExtensionType.GLB;
    S3DirectoryType testS3DirectoryType = S3DirectoryType.AVATAR;
    GenderType testGenderType = testAvatarCreateDto.getGenderType();
    PublicType testPublicType = testAvatarCreateDto.getPublicType();

    Avatar testAvatar =
        Avatar.builder()
            .assetType(testAssetType)
            .s3AssetUUID(testFileUuid)
            .extension(testExtension)
            .s3DirectoryType(testS3DirectoryType)
            .genderType(testGenderType)
            .publicType(testPublicType)
            .build();

    MemberAsset testMemberAsset =
        MemberAsset.builder()
            .asset(testAvatar)
            .member(testMember)
            .build();

    given(avatarRepository.save(any(Avatar.class))).willReturn(testAvatar);
    given(memberAssetRepository.save(any(MemberAsset.class))).willReturn(testMemberAsset);

    avatarService.createAvatar(testAvatarCreateDto, testMember);
  }

  @Nested
  class findOneAvatar {

    @Test
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    void whenNotFoundMember() {
      UUID testAssetId = UUID.randomUUID();

      given(avatarRepository.findById(testAssetId)).willReturn(Optional.empty());

      assertThatThrownBy(
              () -> {
                avatarService.findOneAvatar(testAssetId);
              })
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_AVATAR);
    }

    @Test
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    void whenFoundMember() {
      UUID testAssetId = UUID.randomUUID();
      Avatar testAvatar =
          Avatar.builder()
              .id(testAssetId)
              .assetType(AssetType.AVATAR)
              .extension(ExtensionType.GLB)
              .genderType(GenderType.MALE)
              .publicType(PublicType.PRIVATE)
              .s3AssetUUID(UUID.randomUUID())
              .s3DirectoryType(S3DirectoryType.AVATAR)
              .build();

      given(avatarRepository.findById(testAssetId)).willReturn(Optional.of(testAvatar));

      AvatarResponseDto testFindAvatar = avatarService.findOneAvatar(testAssetId);

      assertThat(testAvatar.getId()).isEqualTo(testFindAvatar.getAssetId());
      assertThat(testAvatar.getAssetType()).isEqualTo(testFindAvatar.getAssetType());
      assertThat(testAvatar.getExtension()).isEqualTo(testFindAvatar.getExtension());
      assertThat(testAvatar.getGenderType()).isEqualTo(testFindAvatar.getGenderType());
      assertThat(testAvatar.getPublicType()).isEqualTo(testFindAvatar.getPublicType());
      assertThat(testAvatar.getS3AssetUUID()).isEqualTo(testFindAvatar.getS3AssetUUID());
      assertThat(testAvatar.getS3DirectoryType()).isEqualTo(testFindAvatar.getS3DirectoryType());
    }
  }
}
