package br.com.springessentials2.demo.configsecurity;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
               // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse() em produção usa-se desta forma
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("password encoder{}", passwordEncoder.encode("20030927"));

        auth.inMemoryAuthentication()
                .withUser("Renan")
                .password(passwordEncoder.encode("20030927"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("Emerson")
                .password(passwordEncoder.encode("bradstore"))
                .roles("USER");
    }
}
