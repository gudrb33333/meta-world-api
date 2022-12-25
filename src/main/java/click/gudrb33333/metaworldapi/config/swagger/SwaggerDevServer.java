package click.gudrb33333.metaworldapi.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

@Component
@Profile("dev")
public class SwaggerDevServer implements WebMvcOpenApiTransformationFilter {

  @Override
  public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
    OpenAPI openApi = context.getSpecification();
    Server localServer = new Server();
    localServer.setDescription("dev");
    localServer.setUrl("http://localhost:4300");

    openApi.setServers(List.of(localServer));
    return openApi;
  }

  @Override
  public boolean supports(DocumentationType documentationType) {
    return documentationType.equals(DocumentationType.OAS_30);
  }
}
