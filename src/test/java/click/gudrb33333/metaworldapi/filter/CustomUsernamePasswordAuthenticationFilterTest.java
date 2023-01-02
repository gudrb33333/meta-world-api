package click.gudrb33333.metaworldapi.filter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.api.v1.auth.dto.MemberLoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.MimeTypeUtils;

@ExtendWith(MockitoExtension.class)
class CustomUsernamePasswordAuthenticationFilterTest {

  @InjectMocks
  private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter;

  @Mock private ObjectMapper objectMapper;

  MockHttpServletRequest request;
  MockHttpServletResponse response;

  @BeforeEach
  void setHttpServlet() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Nested
  class attemptAuthentication {

    @Test
    void whenRequestMethodNotPost() {
      request.setMethod("GET");

      assertThatThrownBy(
              () -> {
                customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response);
              })
          .isInstanceOf(AuthenticationServiceException.class)
          .hasMessageContaining("Authentication method not supported:" + request.getMethod());
    }

    @Test
    void whenRequestContentTypeIsNotJson() {
      request.setMethod("POST");
      request.setContentType(String.valueOf(MimeTypeUtils.TEXT_PLAIN));

      assertThatThrownBy(
              () -> {
                customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response);
              })
          .isInstanceOf(AuthenticationServiceException.class)
          .hasMessageContaining("Authentication request is not JSON");
    }

    @Test
    void whenObjectMapperThrowException() {
      request.setMethod("POST");
      request.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);

      assertThatThrownBy(
          () -> {
            customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response);
          })
          .isInstanceOf(AuthenticationServiceException.class)
          .hasMessageContaining("Request Content-Type(application/json) Parsing Error");
    }
  }
}
