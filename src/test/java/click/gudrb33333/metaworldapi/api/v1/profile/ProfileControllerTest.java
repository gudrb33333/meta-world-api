package click.gudrb33333.metaworldapi.api.v1.profile;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileCreateDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileResponseDto;
import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileUpdateDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockBean ProfileService profileService;

  MockHttpSession mockHttpSession;

  @BeforeEach
  public void setSession() {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    mockHttpSession = (MockHttpSession) attr.getRequest().getSession();
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void create() throws Exception {
    ProfileCreateDto testProfileCreateDto = ProfileCreateDto.builder().nickname("testName").build();

    String content = objectMapper.writeValueAsString(testProfileCreateDto);

    mockMvc
        .perform(post("/api/v1/profiles").content(content).session(mockHttpSession))
        .andExpect(status().isCreated());
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void findSigninMemberProfile() throws Exception {
    Member member = (Member) mockHttpSession.getAttribute("current-member");

    ProfileResponseDto mockProfileResponseDto =
        ProfileResponseDto.builder().signedAvatarUrl("testSignedUrl").nickname("testName").build();

    String expectedSignedAvatarUrl = mockProfileResponseDto.getSignedAvatarUrl();
    String expectedNickname = mockProfileResponseDto.getNickname();

    given(profileService.findSigninMemberProfile(member)).willReturn(mockProfileResponseDto);

    mockMvc
        .perform(get("/api/v1/profiles/me").session(mockHttpSession))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.signedAvatarUrl", is(expectedSignedAvatarUrl)))
        .andExpect(jsonPath("$.nickname", is(expectedNickname)));
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void update() throws Exception {
    ProfileUpdateDto testProfileUpdateDto = ProfileUpdateDto.builder()
        .nickname("testNickname")
        .build();

    String content = objectMapper.writeValueAsString(testProfileUpdateDto);

    mockMvc
        .perform(patch("/api/v1/profiles/me").content(content).session(mockHttpSession))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void deleteSigninMemberProfile() throws Exception {
    mockMvc
        .perform(delete("/api/v1/profiles/me").session(mockHttpSession))
        .andExpect(status().isNoContent());
  }
}
