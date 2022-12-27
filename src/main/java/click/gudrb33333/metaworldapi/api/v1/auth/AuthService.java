package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import click.gudrb33333.metaworldapi.util.PasswordEncoderUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoderUtil passwordEncoderUtil;

  public void signup(MemberCreateDto memberCreateDto) {
    memberRepository
        .findByEmail(memberCreateDto.getEmail())
        .ifPresent(
            m -> {
              throw new CatchedException(ErrorMessage.CONFLICT_EMAIL, HttpStatus.CONFLICT);
            });

    String encodedPassword =
        passwordEncoderUtil.passwordEncoder().encode(memberCreateDto.getPassword());

    Member member =
        Member.builder()
            .email(memberCreateDto.getEmail())
            .password(encodedPassword)
            .loginType(LoginType.LOCAL)
            .role(Role.MEMBER)
            .build();

    memberRepository.save(member);
  }

  // spring security login
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Member findMember =
        memberRepository
            .findByEmailAndLoginType(email, LoginType.LOCAL)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
                });

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + findMember.getRole()));

    return new User(findMember.getEmail(), findMember.getPassword(), authorities);
  }
}
