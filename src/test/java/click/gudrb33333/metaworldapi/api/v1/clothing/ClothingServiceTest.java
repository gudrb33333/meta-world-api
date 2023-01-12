package click.gudrb33333.metaworldapi.api.v1.clothing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingResponseDto;
import click.gudrb33333.metaworldapi.entity.Clothing;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.clothing.ClothingRepository;
import click.gudrb33333.metaworldapi.repository.memberasset.MemberAssetRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.io.IOException;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.jets3t.service.CloudFrontServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class ClothingServiceTest {

  @InjectMocks ClothingService clothingService;

  @Mock private ClothingRepository clothingRepository;

  @Mock private MemberAssetRepository memberAssetRepository;

  @Mock private AwsS3Util awsS3Util;

  @Test
  void createClothing() {
    Member testMember = Member.builder().email("test@test.com").build();
    UUID testFileUuid = UUID.randomUUID();

    ClothingCreateDto testClothingCreateDto =
        ClothingCreateDto.builder()
            .associateLink("testLink")
            .brand("testBrand")
            .genderType(GenderType.MALE)
            .publicType(PublicType.PUBLIC)
            .detailDescription("testDescription")
            .name("testName")
            .price(10000)
            .serialNumber("testSerialNumber")
            .build();

    Clothing savedTestClothing =
        Clothing.builder()
            .id(UUID.randomUUID())
            .assetType(AssetType.CLOTHING)
            .s3AssetUUID(testFileUuid)
            .price(testClothingCreateDto.getPrice())
            .associateLink(testClothingCreateDto.getAssociateLink())
            .brand(testClothingCreateDto.getBrand())
            .extension(ExtensionType.GLB)
            .genderType(testClothingCreateDto.getGenderType())
            .detailDescription(testClothingCreateDto.getDetailDescription())
            .publicType(testClothingCreateDto.getPublicType())
            .name(testClothingCreateDto.getName())
            .s3DirectoryType(S3DirectoryType.CLOTHING)
            .serialNumber(testClothingCreateDto.getSerialNumber())
            .build();

    MockMultipartFile testFile =
        new MockMultipartFile("multipartFile", "filename.txt", "text/plain", "some xml".getBytes());

    given(clothingRepository.save(any(Clothing.class))).willReturn(savedTestClothing);

    MemberAsset saveTestMemberAsset =
        MemberAsset.builder()
            .id(UUID.randomUUID())
            .member(testMember)
            .asset(savedTestClothing)
            .build();

    given(memberAssetRepository.save(any(MemberAsset.class))).willReturn(saveTestMemberAsset);

    clothingService.createClothing(testClothingCreateDto, testFile, testMember);

    assertThat(saveTestMemberAsset.getMember()).isEqualTo(testMember);
    assertThat(saveTestMemberAsset.getAsset()).isEqualTo(savedTestClothing);
  }

  @Nested
  class findOneClothing {

    Member testMember = Member.builder().email("test@test.com").build();
    UUID testUUID = UUID.randomUUID();

    @Test
    void whenClothingNotFound() {
      given(clothingRepository.findById(testUUID)).willReturn(Optional.empty());

      assertThatThrownBy(
              () -> {
                clothingService.findOneClothing(testUUID, testMember);
              })
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_CLOTHING);
    }

    @Nested
    class whenClothingExistAndPublicTypeIsPrivate {

      Clothing testClothing =
          Clothing.builder()
              .id(testUUID)
              .s3AssetUUID(UUID.randomUUID())
              .publicType(PublicType.PRIVATE)
              .brand("testBrand")
              .detailDescription("testDetailDescription")
              .name("testName")
              .price(1000)
              .serialNumber("testSerialNumber")
              .associateLink("testAssociateLink")
              .build();

      @Test
      void andMemberAssetNotFound() {
        given(clothingRepository.findById(testUUID)).willReturn(Optional.of(testClothing));

        given(memberAssetRepository.findByMemberAndAsset(testMember, testClothing))
            .willReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                  clothingService.findOneClothing(testUUID, testMember);
                })
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining(ErrorMessage.ACCESS_DENIED);
      }

      @Test
      void andMemberAssetExist() throws IOException, ParseException, CloudFrontServiceException {
        MemberAsset testMemberAsset =
            MemberAsset.builder()
                .id(UUID.randomUUID())
                .member(testMember)
                .asset(testClothing)
                .build();

        given(clothingRepository.findById(testUUID)).willReturn(Optional.of(testClothing));

        given(memberAssetRepository.findByMemberAndAsset(testMember, testClothing))
            .willReturn(Optional.of(testMemberAsset));

        given(
                awsS3Util.createSignedUrl(
                    S3DirectoryType.CLOTHING, testClothing.getS3AssetUUID(), 86400))
            .willReturn("testSignedUrl");

        ClothingResponseDto clothingResponseDto =
            clothingService.findOneClothing(testUUID, testMember);

        assertThat(clothingResponseDto.getId()).isEqualTo(testClothing.getId());
        assertThat(clothingResponseDto.getSignedClothingUrl()).isEqualTo("testSignedUrl");
      }
    }

    @Test
    void whenClothingExistAndPublicTypeIsPublic()
        throws IOException, ParseException, CloudFrontServiceException {
      Clothing testClothing =
          Clothing.builder()
              .id(testUUID)
              .s3AssetUUID(UUID.randomUUID())
              .publicType(PublicType.PUBLIC)
              .brand("testBrand")
              .detailDescription("testDetailDescription")
              .name("testName")
              .price(1000)
              .serialNumber("testSerialNumber")
              .associateLink("testAssociateLink")
              .build();

      given(clothingRepository.findById(testUUID)).willReturn(Optional.of(testClothing));

      given(
              awsS3Util.createSignedUrl(
                  S3DirectoryType.CLOTHING, testClothing.getS3AssetUUID(), 86400))
          .willReturn("testSignedUrl");

      ClothingResponseDto clothingResponseDto =
          clothingService.findOneClothing(testUUID, testMember);

      assertThat(clothingResponseDto.getId()).isEqualTo(testClothing.getId());
      assertThat(clothingResponseDto.getSignedClothingUrl()).isEqualTo("testSignedUrl");
    }
  }
}
