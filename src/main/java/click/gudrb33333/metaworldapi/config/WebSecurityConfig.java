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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthService authService;
  private final PasswordEncoderUtil passwordEncoderUtil;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CorsConfig corsConfig;

  private static final String[] PERMIT_ALL_LIST = {
    "/health",
    "/swagger*/**",
    "/v3/api-docs",
    "/api/v1/auth/signup",
    "/api/v1/auth/signin",
    "/api/v1/members/me",
  };
  private static final String[] PERMIT_ADMIN_AND_MEMBER_LIST = {"/api/v1/auth/logout"};
  private static final String[] PERMIT_ADMIN_LIST = {};
  private final String roleAdmin = String.valueOf(Role.ADMIN);
  private final String roleMember = String.valueOf(Role.MEMBER);

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
        .configurationSource(corsConfig.corsConfigurationSource())
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

  protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() throws Exception {
    CustomUsernamePasswordAuthenticationFilter authFilter =
        new CustomUsernamePasswordAuthenticationFilter();

    authFilter.setFilterProcessesUrl("/api/v1/auth/signin");
    authFilter.setAuthenticationManager(this.authenticationManagerBean());
    authFilter.setUsernameParameter("email");
    authFilter.setPasswordParameter("password");
    authFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

    return authFilter;
  }
}
