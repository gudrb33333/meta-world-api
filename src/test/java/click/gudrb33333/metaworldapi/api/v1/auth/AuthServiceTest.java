package click.gudrb33333.metaworldapi.api.v1.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.exception.CommonException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.member.MemberRepository;
import click.gudrb33333.metaworldapi.util.PasswordEncoderUtil;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @InjectMocks private AuthService authService;

  @Mock private MemberRepository memberRepository;

  @Spy private PasswordEncoderUtil passwordEncoderUtil;

  @Nested
  class signup {

    MemberCreateDto testMemberCreateDto =
        MemberCreateDto.builder().email("test@test.com").password("testPassword").build();

    Member testMember = Member.builder().email("test@test.com").build();

    @Test
    void whenEmailIsConflict() {
      given(memberRepository.findByEmail(testMemberCreateDto.getEmail()))
          .willReturn(Optional.of(testMember));

      assertThatThrownBy(
              () -> {
                authService.signup(testMemberCreateDto);
              })
          .isInstanceOf(CommonException.class)
          .hasMessageContaining(ErrorMessage.CONFLICT_EMAIL);
    }

    @Test
    void whenEmailIsNotConflict() {
      Member testMember =
          Member.builder()
              .email(testMemberCreateDto.getEmail())
              .password(testMemberCreateDto.getPassword())
              .loginType(LoginType.LOCAL)
              .role(Role.MEMBER)
              .build();

      given(memberRepository.save(any(Member.class))).willReturn(testMember);

      authService.signup(testMemberCreateDto);
    }
  }

  @Nested
  class loadUserByUsername {

    Member testMember =
        Member.builder().email("test@test.com").password("testPassword").role(Role.MEMBER).build();

    @Test
    void whenMemberNotFound() {

      assertThatThrownBy(
              () -> {
                authService.loadUserByUsername(testMember.getEmail());
              })
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining(ErrorMessage.NOT_FOUND_MEMBER);
    }

    @Test
    void whenMemberFound() {
      given(memberRepository.findByEmailAndLoginType(testMember.getEmail(), LoginType.LOCAL)).willReturn(
          Optional.of(testMember));

      User testSecurityUser = (User) authService.loadUserByUsername(testMember.getEmail());

      assertThat(testSecurityUser.getUsername()).isEqualTo(testMember.getEmail());
      assertThat(testSecurityUser.getPassword()).isEqualTo(testMember.getPassword());
      // TODO: Add role assertion
    }
  }
}
