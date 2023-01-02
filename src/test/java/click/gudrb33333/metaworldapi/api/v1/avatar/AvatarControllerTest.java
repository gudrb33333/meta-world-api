package click.gudrb33333.metaworldapi.api.v1.avatar;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarCreateDto;
import click.gudrb33333.metaworldapi.api.v1.avatar.dto.AvatarResponseDto;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AvatarControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  AvatarService avatarService;

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void create() throws Exception {
    AvatarCreateDto testAvatarCreateDto =
        AvatarCreateDto.builder()
            .avatarUrl("someUrl")
            .genderType(GenderType.MALE)
            .publicType(PublicType.PRIVATE)
            .build();

    String content = objectMapper.writeValueAsString(testAvatarCreateDto);

    mockMvc.perform(post("/api/v1/avatars").content(content)).andExpect(status().isCreated());
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void findOne() throws Exception {
    UUID mockAssetId = UUID.randomUUID();
    UUID mockS3AssetUUID = UUID.randomUUID();
    AvatarResponseDto avatarResponseDto =
        AvatarResponseDto.builder()
            .assetId(mockAssetId)
            .genderType(GenderType.MALE)
            .s3AssetUUID(mockS3AssetUUID)
            .publicType(PublicType.PRIVATE)
            .assetType(AssetType.AVATAR)
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .build();

    given(avatarService.findOneAvatar(mockAssetId)).willReturn(avatarResponseDto);

    String ExpectedAssetId = String.valueOf(avatarResponseDto.getAssetId());
    String ExpectedGenderType = avatarResponseDto.getGenderType().getValue();
    String ExpectedS3AssetUUID = String.valueOf(avatarResponseDto.getS3AssetUUID());
    String ExpectedPublicType = avatarResponseDto.getPublicType().getValue();
    String ExpectedExtension = avatarResponseDto.getExtension().getValue();
    String ExpectedAssetType = String.valueOf(avatarResponseDto.getAssetType());
    String ExpectedS3DirectoryType = avatarResponseDto.getS3DirectoryType().getValue();

    mockMvc
        .perform(get("/api/v1/avatars/" + mockAssetId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.assetId", is(ExpectedAssetId)))
        .andExpect(jsonPath("$.genderType", is(ExpectedGenderType)))
        .andExpect(jsonPath("$.s3AssetUUID", is(ExpectedS3AssetUUID)))
        .andExpect(jsonPath("$.publicType", is(ExpectedPublicType)))
        .andExpect(jsonPath("$.extension", is(ExpectedExtension)))
        .andExpect(jsonPath("$.assetType", is(ExpectedAssetType)))
        .andExpect(jsonPath("$.s3DirectoryType", is(ExpectedS3DirectoryType)));
  }
}
