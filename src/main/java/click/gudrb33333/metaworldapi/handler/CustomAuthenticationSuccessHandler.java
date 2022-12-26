package click.gudrb33333.metaworldapi.handler;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.repository.MemberRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    HttpSession session = request.getSession();
    User userSpringSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Member member =
        memberRepository
            .findByEmailAndLoginType(userSpringSecurity.getUsername(), LoginType.LOCAL)
            .orElseThrow(
                () -> {
                  throw new CatchedException("member not found");
                });

    session.setAttribute("role", String.valueOf( userSpringSecurity.getAuthorities()));
    session.setAttribute("connected-user" , member);
  }
}
