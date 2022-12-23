package click.gudrb33333.metaworldapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String[] PERMIT_ALL_LIST = {
      "/health",
      "/swagger*/**",
      "/v3/api-docs",
  };

  private static final String[] PERMIT_ADMIN_LIST = {};

  private static final String[] PERMIT_ADMIN_AND_MEMBER_LIST = {};

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    //configuration.addAllowedOriginPattern("*");
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    //security 접근 url
    http.httpBasic().disable() //httpBasic().disable() rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
        .cors().configurationSource(corsConfigurationSource()) // cors 설정
        .and()
        .csrf().disable() //rest api이므로 csrf 보안이 필요없으므로 disable처리.
        .authorizeRequests() //다음 리퀘스트에 대한 사용권한 체크
        .antMatchers(PERMIT_ALL_LIST).permitAll() //모두 접근 가능
        .antMatchers(PERMIT_ADMIN_LIST).hasAnyRole("ADMIN") //ADMIN만 접근 가능
        .antMatchers(PERMIT_ADMIN_AND_MEMBER_LIST).hasAnyRole("ADMIN,MEMBER") //ADMIN,MEMBER만 접근 가능
        .anyRequest()
        .hasRole("MEMBER"); // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
  }
}
