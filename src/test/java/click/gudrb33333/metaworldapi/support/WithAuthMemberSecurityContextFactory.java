package click.gudrb33333.metaworldapi.support;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.Role;
import java.util.List;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WithAuthMemberSecurityContextFactory
    implements WithSecurityContextFactory<WithAuthMember> {

  protected MockHttpSession session;
  protected MockHttpServletRequest request;

  @Override
  public SecurityContext createSecurityContext(WithAuthMember annotation) {
    String email = annotation.email();
    Role role = annotation.role();

    Member member = Member.builder().email(email).build();

    session = new MockHttpSession();
    session.setAttribute("current-member", member);

    request = new MockHttpServletRequest();
    request.setSession(session);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    UsernamePasswordAuthenticationToken authRequest =
        new UsernamePasswordAuthenticationToken(
            email, "password", List.of(new SimpleGrantedAuthority("ROLE_" + role.value())));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authRequest);

    return context;
  }
}
