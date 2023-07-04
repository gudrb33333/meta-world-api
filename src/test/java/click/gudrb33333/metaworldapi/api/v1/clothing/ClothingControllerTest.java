package click.gudrb33333.metaworldapi.api.v1.clothing;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingCreateDto;
import click.gudrb33333.metaworldapi.api.v1.clothing.dto.ClothingResponseDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClothingControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ClothingService clothingService;

  private MockHttpSession mockHttpSession;

  @BeforeEach
  public void setSession() {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    mockHttpSession = (MockHttpSession) attr.getRequest().getSession();
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void create() throws Exception {
    Member testMember = (Member) mockHttpSession.getAttribute("current-member");

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

    String content = objectMapper.writeValueAsString(testClothingCreateDto);

    MockMultipartFile testFile =
        new MockMultipartFile("multipartFile", "filename.txt", "text/plain", "some xml".getBytes());

    MockMultipartFile jsonFile =
        new MockMultipartFile(
            "clothingCreateDto",
            "clothingCreateDto",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));

    willDoNothing()
        .given(clothingService)
        .createClothing(testClothingCreateDto, testFile, testMember);

    mockMvc
        .perform(multipart("/api/v1/clothing").file(testFile).file(jsonFile))
        .andExpect(status().isCreated());
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void findOne() throws Exception {
    Member testMember = (Member) mockHttpSession.getAttribute("current-member");
    UUID testAssetId = UUID.randomUUID();
    ClothingResponseDto testClothingResponseDto =
        ClothingResponseDto.builder()
            .id(testAssetId)
            .signedClothingUrl("testSignedUrl")
            .associateLink("testAssociateLink")
            .serialNumber("testSerialNumber")
            .price(10000)
            .name("testName")
            .detailDescription("testDetailDescription")
            .brand("testBrand")
            .build();

    given(clothingService.findOneClothing(testAssetId, testMember))
        .willReturn(testClothingResponseDto);

    mockMvc
        .perform(
            get("/api/v1/clothing/" + testAssetId)
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(String.valueOf(testAssetId)))
        .andExpect(jsonPath("$.signedClothingUrl").value("testSignedUrl"))
        .andExpect(jsonPath("$.associateLink").value("testAssociateLink"))
        .andExpect(jsonPath("$.serialNumber").value("testSerialNumber"))
        .andExpect(jsonPath("$.price").value("10000"))
        .andExpect(jsonPath("$.name").value("testName"))
        .andExpect(jsonPath("$.detailDescription").value("testDetailDescription"))
        .andExpect(jsonPath("$.brand").value("testBrand"));
  }
}
