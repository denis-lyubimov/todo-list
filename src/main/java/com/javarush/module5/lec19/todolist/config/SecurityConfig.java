package com.javarush.module5.lec19.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user1 = User.builder()
                .username("admin")
                .password(encoder().encode("passwd"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.builder()
                .username("user")
                .password(encoder().encode("passwd"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/todo-list").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/delete/**","/addtask").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
//                .formLogin(Customizer.withDefaults()) // не упоротая дефолтная форма
//                .httpBasic(Customizer.withDefaults()) // упоротая форма по сешнайди
                .formLogin(o -> o
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/todo-list", true)
                        .failureForwardUrl("/login")
                        .permitAll()
                ).logout(o->o
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );

        return http.build();

    }



}
