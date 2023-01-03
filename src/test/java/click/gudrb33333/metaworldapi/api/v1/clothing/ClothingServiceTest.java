package click.gudrb33333.metaworldapi.api.v1.clothing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.entity.Clothing;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.repository.ClothingRepository;
import click.gudrb33333.metaworldapi.repository.MemberAssetRepository;
import click.gudrb33333.metaworldapi.util.AwsS3Util;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

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

    Clothing saveTestClothing =
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

    given(clothingRepository.save(any(Clothing.class))).willReturn(saveTestClothing);

    MemberAsset saveTestMemberAsset =
        MemberAsset.builder()
            .id(UUID.randomUUID())
            .member(testMember)
            .asset(saveTestClothing)
            .build();

    given(memberAssetRepository.save(any(MemberAsset.class))).willReturn(saveTestMemberAsset);

    clothingService.createClothing(testClothingCreateDto, testFile, testMember);

    assertThat(saveTestMemberAsset.getMember()).isEqualTo(testMember);
    assertThat(saveTestMemberAsset.getAsset()).isEqualTo(saveTestClothing);
  }
}
