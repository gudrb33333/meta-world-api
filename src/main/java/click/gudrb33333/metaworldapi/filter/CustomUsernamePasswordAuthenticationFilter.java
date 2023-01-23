package click.gudrb33333.metaworldapi.filter;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
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

public class CustomUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          ErrorMessage.NOT_SUPPORT_METHOD + request.getMethod());
    }

    if (!request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
      throw new AuthenticationServiceException(ErrorMessage.REQUEST_IS_NOT_JSON);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      MemberLoginDto memberLoginDto =
          objectMapper.readValue(
              request.getReader().lines().collect(Collectors.joining()), MemberLoginDto.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(
              memberLoginDto.getEmail(), memberLoginDto.getPassword());

      setDetails(request, authRequest);

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException(ErrorMessage.REQUEST_CONTENT_TYPE_PARSING_ERROR);
    }
  }

  //  //Unused
  //  @Override
  //  public void setPostOnly(boolean postOnly) {
  //    this.postOnly = postOnly;
  //  }
}
