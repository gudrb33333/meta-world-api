package click.gudrb33333.metaworldapi.config;

import click.gudrb33333.metaworldapi.api.v1.auth.AuthService;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.filter.CustomUsernamePasswordAuthenticationFilter;
import click.gudrb33333.metaworldapi.handler.CustomAuthenticationSuccessHandler;
import click.gudrb33333.metaworldapi.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthService authService;
  private final PasswordEncoderUtil passwordEncoderUtil;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  private static final String[] PERMIT_ALL_LIST = {
      "/health",
      "/swagger*/**",
      "/v3/api-docs",
      "/api/v1/auth/signup",
      "/api/v1/auth/signin",
  };
  private static final String[] PERMIT_ADMIN_AND_MEMBER_LIST = {
      "/api/v1/auth/logout",
  };
  private static final String[] PERMIT_ADMIN_LIST = {};
  private final String roleAdmin = String.valueOf(Role.ADMIN);
  private final String roleMember = String.valueOf(Role.MEMBER);

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    //configuration.addAllowedOriginPattern("https://meta-world.gudrb33333.click");
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.sessionManagement()
        .maximumSessions(2000)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .expiredUrl("/");

    // security access url
    http.httpBasic()
        .disable()
        .cors()
        .configurationSource(corsConfigurationSource())
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(PERMIT_ALL_LIST)
        .permitAll()
        .antMatchers(PERMIT_ADMIN_LIST)
        .hasAnyRole(roleAdmin)
        .antMatchers(PERMIT_ADMIN_AND_MEMBER_LIST)
        .hasAnyRole(roleAdmin, roleMember)
        .anyRequest()
        .hasRole(roleMember)
        .and()
        .addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(authService).passwordEncoder(passwordEncoderUtil.passwordEncoder());
  }

  protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() {
    CustomUsernamePasswordAuthenticationFilter authFilter = new CustomUsernamePasswordAuthenticationFilter();

    try {
      authFilter.setFilterProcessesUrl("/api/v1/auth/signin");
      authFilter.setAuthenticationManager(this.authenticationManagerBean());
      authFilter.setUsernameParameter("email");
      authFilter.setPasswordParameter("password");
      authFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
      //authFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

    }catch (Exception e) {
      e.printStackTrace();
    }

    return authFilter;
  }
}
