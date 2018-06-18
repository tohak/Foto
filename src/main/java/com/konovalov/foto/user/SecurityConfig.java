package com.konovalov.foto.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


//настройка security
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getShaPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .csrf().disable() // отключить защиту
                    .authorizeRequests() // без авторизации нечего нельзя делать
                    .antMatchers("/").hasAnyRole("USER", "ADMIN") // перейти на эту страницу может только пользователи групп
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/register").permitAll() // перейти могут все
                    .and()
                .exceptionHandling().accessDeniedPage("/unauthorized") // если зашел без доступа перекидывать на страницу
                    .and()
                .formLogin() // форма логина
                    .loginPage("/login") // страница
                    .loginProcessingUrl("/j_spring_security_check")  // пометить что это форма логина
                    .failureUrl("/login?error")   // как выглядит страница с ошибкой
                    .usernameParameter("j_login")
                    .passwordParameter("j_password")
                    .permitAll() // форма доступна всем
                    .and()
                .logout()
                    .permitAll()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true); //закрыть сессию
    }

    private ShaPasswordEncoder getShaPasswordEncoder() {
        return new ShaPasswordEncoder();
    }
}
