package com.tms.oknapvh.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // UserServiceImpl is already a bean and implements UserDetailsService
    // private final UserServiceImpl userService; // No longer need to inject here for AuthenticationManagerBuilder

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/store", "/store/search", "/store/register", "/store/sign-in", "/store/window-details/**", "/store/profile/*", "/css/**", "/js/**", "/images/**", "/fragment/**", "/static/**").permitAll()
                        .requestMatchers("/store/orders/**", "/store/profile/").authenticated()
                        .requestMatchers("/store/redactor/**", "/store/users-list").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // allows sending requests from an authorized user
                .formLogin(formLogin -> formLogin
                        .loginPage("/store/sign-in")
                        .loginProcessingUrl("/store/sign-in")
                        .failureUrl("/store/sign-in?error=true")
                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                        .defaultSuccessUrl("/store", false)
                )
                .logout(logout -> logout
                        .logoutUrl("/store/logout")
                )
                // "Remember me" function
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret") // Set a secret key
                        .tokenValiditySeconds(86400) // Key validity period in seconds
                )
                .httpBasic(httpBasic -> {}); // Use withDefaults() or a customizer if further config needed
        return http.build();
    }

    // configure(AuthenticationManagerBuilder auth) is no longer overridden.
    // Spring Security will automatically use the UserDetailsService bean (UserServiceImpl)
    // and the PasswordEncoder bean defined above.
}
