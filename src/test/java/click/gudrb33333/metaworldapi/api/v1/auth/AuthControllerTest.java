package click.gudrb33333.metaworldapi.api.v1.auth;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.support.WithAuthMember;
import click.gudrb33333.metaworldapi.util.PasswordEncoderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AuthService authService;

  @Autowired private PasswordEncoderUtil passwordEncoderUtil;

  @Test
  void signup() throws Exception {
    MemberCreateDto testMemberCreateDto =
        MemberCreateDto.builder().email("test@test.com").password("testPassword").build();

    String content = objectMapper.writeValueAsString(testMemberCreateDto);

    willDoNothing().given(authService).signup(testMemberCreateDto);

    mockMvc.perform(post("/api/v1/auth/signup").content(content)).andExpect(status().isCreated());
  }

  @Nested
  class signin {

    private final MemberLoginDto testMemberLoginDto =
        MemberLoginDto.builder().email("test@test.com").password("testPassword").build();

    private final Member member =
        Member.builder().email("test@test.com").password("testPassword").build();

    @Test
    void whenFoundMember() throws Exception {
      String content = objectMapper.writeValueAsString(testMemberLoginDto);

      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

      String testEncodedTestPassword =
          passwordEncoderUtil.passwordEncoder().encode(testMemberLoginDto.getPassword());

      given(authService.loadUserByUsername(testMemberLoginDto.getEmail()))
          .willReturn(
              new User(testMemberLoginDto.getEmail(), testEncodedTestPassword, authorities));

      mockMvc
          .perform(
              post("/api/v1/auth/signin").contentType(MediaType.APPLICATION_JSON).content(content))
          .andExpect(status().isOk());
    }

    @Test
    void whenNotFoundMember() throws Exception {
      String content = objectMapper.writeValueAsString(testMemberLoginDto);

      given(authService.loadUserByUsername(testMemberLoginDto.getEmail())).willReturn(null);

      mockMvc
          .perform(
              post("/api/v1/auth/signin").contentType(MediaType.APPLICATION_JSON).content(content))
          .andExpect(status().isUnauthorized());
    }
  }

  @Test
  @WithAuthMember(email = "test@test.com", role = Role.MEMBER)
  void logout() throws Exception {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    MockHttpSession mockHttpSession = (MockHttpSession) attr.getRequest().getSession();

    mockMvc
        .perform(delete("/api/v1/auth/logout").session(mockHttpSession))
        .andExpect(status().isOk());
  }
}
