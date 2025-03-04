package com.utin.oj.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/user/login").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager (UserDetailsService userDetailsService){
        var myOwnAuthenticationProvider = new MyOwnAuthenticationProvider(userDetailsService);
        return new ProviderManager(myOwnAuthenticationProvider);
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        var utin = User.withDefaultPasswordEncoder()
//                .username("utin")
//                .password("Pass123")
//                .roles("USER")
//                .build();
//
//        var admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(List.of(utin,admin));
//    }
//    OR

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return  new InMemoryUserDetailsManager(
                User.withUsername("utin").password("Pass123").roles("USER").build(),
                User.withUsername("admin").password("admin").roles("USER").build()
        );
    }
}
