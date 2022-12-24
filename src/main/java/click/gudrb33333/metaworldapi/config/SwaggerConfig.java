package click.gudrb33333.metaworldapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

  @Value("${ext.appVersion}")
  private String appVersion;

  @Bean
  public Docket restApiV1() {
    String version = "V1";

    return new Docket(DocumentationType.OAS_30) // open api spec 3.0
        .apiInfo(apiInfo())
        .groupName(version)
        .select()
        .apis(RequestHandlerSelectors.basePackage("click.gudrb33333.metaworldapi.api")
            .or(RequestHandlerSelectors.basePackage("click.gudrb33333.metaworldapi.api.v1")))
        .paths(PathSelectors.ant("/*")
            .or(PathSelectors.ant("/api/v1/**")))
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("META WORLD REST API")
        .version(appVersion)
        .description("")
        .build();
  }
}
