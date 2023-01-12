package click.gudrb33333.metaworldapi.handler;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.exception.CommonException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.member.MemberRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    HttpSession session = request.getSession();
    User userSpringSecurity =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Member member =
        memberRepository
            .findByEmailAndLoginType(userSpringSecurity.getUsername(), LoginType.LOCAL)
            .orElseThrow(
                () -> {
                  throw new CommonException(ErrorMessage.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
                });

    session.setAttribute("role", String.valueOf(userSpringSecurity.getAuthorities()));
    session.setAttribute("current-member", member);
  }
}
