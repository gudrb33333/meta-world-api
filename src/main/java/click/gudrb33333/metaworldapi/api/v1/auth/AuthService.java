package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberCreateDto;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;

  public void signup(MemberCreateDto memberCreateDto) {
    memberRepository
        .findByEmail(memberCreateDto.getEmail())
        .ifPresent(
            m -> {
              throw new CatchedException(
                  "email already exists(unique key violation)", HttpStatus.CONFLICT);
            });

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encodedPassword = passwordEncoder.encode(memberCreateDto.getPassword());

    Member member =
        Member.builder()
            .email(memberCreateDto.getEmail())
            .password(encodedPassword)
            .loginType("local")
            .role(Role.ROLE_USER)
            .build();

    memberRepository.save(member);
  }
}
