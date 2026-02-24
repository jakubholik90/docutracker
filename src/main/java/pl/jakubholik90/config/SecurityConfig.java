package pl.jakubholik90.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // enables spring security filter chain, web seciruty configuration, default safe mechanisms
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //Authorize requests
                .authorizeHttpRequests(auth->auth
                                .requestMatchers("/", "/login", "/error", "/webjars/**", "/css/**", "/js/**").permitAll() //public
                                .anyRequest().authenticated() //other require authentication
                )
                //OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") //login page
                        .defaultSuccessUrl("/home", true)//after succesful logging
                )
                //Logout
                .logout(logout->logout
                        .logoutSuccessUrl("/") //after logging out
                        .permitAll()
                );

        return http.build();

    }

}
