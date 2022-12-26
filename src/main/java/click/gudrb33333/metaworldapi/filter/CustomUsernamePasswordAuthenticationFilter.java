package click.gudrb33333.metaworldapi.filter;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.MimeTypeUtils;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private boolean postOnly = true;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (postOnly && !request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported:" + request.getMethod());
    }

    if(!request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)){
      throw new AuthenticationServiceException(
          "Authentication request is not JSON");
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      MemberLoginDto memberLoginDto =
          objectMapper.readValue(
              request.getReader().lines().collect(Collectors.joining()), MemberLoginDto.class);

      log.info("[LOGIN_REQUEST] [loginId:{}, password:******]", memberLoginDto.getEmail());

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(
              memberLoginDto.getEmail(), memberLoginDto.getPassword());

      setDetails(request, authRequest);

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException(
          "Request Content-Type(application/json) Parsing Error");
    }
  }

  @Override
  public void setPostOnly(boolean postOnly) {
    this.postOnly = postOnly;
  }
}
