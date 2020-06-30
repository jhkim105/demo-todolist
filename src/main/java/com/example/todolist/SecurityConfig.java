package com.example.todolist;


import com.example.todolist.api.security.TokenAuthenticationEntryPoint;
import com.example.todolist.api.security.TokenAuthenticationFilter;
import com.example.todolist.api.security.TokenAuthenticationProvider;
import com.example.todolist.api.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/resources/**", "/js/**", "/css/**", "/webjars/**",
        "/error","/swagger-ui.html", "/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", "/v2/api-docs/**", "/favicon.ico");
  }

  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .authorizeRequests()
        .antMatchers("/", "/auth/**", "/login", "/todo-list", "/oauth2-login").permitAll()
        .anyRequest().authenticated()
        .and()
      .addFilterBefore(tokenAuthenticationFilter(), BasicAuthenticationFilter.class)
      .authenticationProvider(tokenAuthenticationProvider())
      .httpBasic()
        .authenticationEntryPoint(tokenAuthenticationEntryPoint())
        .and()
      .csrf().disable()
      .oauth2Login()
        .loginPage("/oauth2-login");
    // @formatter:on
  }

  @Bean
  public UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter("/**");
    return tokenAuthenticationFilter;
  }

  @Bean
  public TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint() {
    return new TokenAuthenticationEntryPoint();
  }

  @Bean
  public TokenAuthenticationProvider tokenAuthenticationProvider() {
    return new TokenAuthenticationProvider();
  }

}
