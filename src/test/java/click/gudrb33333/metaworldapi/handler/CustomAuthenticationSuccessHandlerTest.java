package click.gudrb33333.metaworldapi.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

  @InjectMocks private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Mock private MemberRepository memberRepository;

  MockHttpServletRequest request;
  MockHttpServletResponse response;
  UsernamePasswordAuthenticationToken authRequest;

  @BeforeEach
  void setAuth() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

    authRequest =
        new UsernamePasswordAuthenticationToken(
            new User("test@test.com", "testPassword", authorities), "testPassword");

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authRequest);
  }

  @Nested
  class onAuthenticationSuccess {

    @Test
    void whenMemberExist() {
      Member member =
          Member.builder()
              .email("test@test.com")
              .password("testPassword")
              .loginType(LoginType.LOCAL)
              .role(Role.MEMBER)
              .build();

      given(memberRepository.findByEmailAndLoginType("test@test.com", LoginType.LOCAL))
          .willReturn(Optional.of(member));

      customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authRequest);
    }

    @Test
    void whenMemberNotExist() {
      given(memberRepository.findByEmailAndLoginType("test@test.com", LoginType.LOCAL))
          .willReturn(Optional.empty());

      assertThatThrownBy(
              () -> {
                customAuthenticationSuccessHandler.onAuthenticationSuccess(
                    request, response, authRequest);
              })
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_MEMBER);
    }
  }
}
