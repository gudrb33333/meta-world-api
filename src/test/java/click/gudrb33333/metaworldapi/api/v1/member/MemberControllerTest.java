package click.gudrb33333.metaworldapi.api.v1.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("MemberControllerTest")
class MemberControllerTest {

  @Autowired private MockMvc mockMvc;

  private MockHttpSession mockHttpSession;

  @BeforeEach
  void setSession() {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    mockHttpSession = (MockHttpSession) attr.getRequest().getSession();
  }

  @Nested
  @DisplayName("로그인 한 사용자를 찾는 테스트")
  class findSigninMember {

    @Test
    @DisplayName("로그인을 안 했을 때")
    public void whenNotSignedIn() throws Exception {
      mockMvc
          .perform(get("/api/v1/members/me").session(mockHttpSession))
          .andExpect(status().isUnauthorized())
          .andExpect(jsonPath("$.message").value("access denied."));
    }

    @Test
    @DisplayName("로그인을 했을 때")
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    public void whenSignedIn() throws Exception {
      mockMvc
          .perform(get("/api/v1/members/me").session(mockHttpSession))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect((ResultMatcher) jsonPath("$.email").exists());
    }
  }
}
