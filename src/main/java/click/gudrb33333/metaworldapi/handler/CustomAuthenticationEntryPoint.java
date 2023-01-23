package click.gudrb33333.metaworldapi.handler;

import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authenticationException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(out, Map.of("message", ErrorMessage.ACCESS_DENIED));
    out.flush();
  }
}
