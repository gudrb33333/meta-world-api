package click.gudrb33333.metaworldapi.api.v1.member;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

  @Autowired MockMvc mockMvc;

  MockHttpSession mockHttpSession;

  @BeforeEach
  void setSession() {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    mockHttpSession = (MockHttpSession) attr.getRequest().getSession();
  }

  @Nested
  class findSigninMember {

    @Test
    public void whenNotSignedIn() throws Exception {
      mockMvc
          .perform(
              get("/api/v1/members/me").session(mockHttpSession))
          .andExpect(status().isNotFound())
          .andExpect(
              result -> assertTrue(result.getResolvedException() instanceof CatchedException))
          .andExpect(
              result ->
                  assertEquals("member not found.", result.getResolvedException().getMessage()));
    }

    @Test
    @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
    public void whenSignedIn() throws Exception {
      mockMvc
          .perform(
              get("/api/v1/members/me").session(mockHttpSession))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect((ResultMatcher) jsonPath("$.email").exists());
    }
  }
}
