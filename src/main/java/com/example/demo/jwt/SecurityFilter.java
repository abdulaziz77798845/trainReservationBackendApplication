package com.example.demo.jwt;


import com.example.demo.role_and_permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig->csrfConfig.disable())
                .sessionManagement(sessionMangConfig->sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authConfig->{

                    authConfig.requestMatchers(HttpMethod.POST,"/auth/register","/auth/authenticate").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST,"/auth/admin/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST,"/auth/add/admin").hasAuthority(Permissions.ADD_ADMINS.name());
                    authConfig.requestMatchers(AUTH_WHITELIST).permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET,"api/trains/getAllTrainsInTheRegion").hasAuthority(Permissions.GET_ALL_TRAINS.name());
                    authConfig.requestMatchers(HttpMethod.POST,"/api/trains/add").hasAuthority(Permissions.ADD_TRAINS.name());
                    authConfig.requestMatchers(HttpMethod.PUT,"/api/trains/update").hasAuthority(Permissions.UPDATE_TRAINS.name());
                    authConfig.requestMatchers(HttpMethod.DELETE,"api/trains/deleteById/{id}").hasAuthority(Permissions.DELETE_TRAINS.name());
                    authConfig.requestMatchers(HttpMethod.POST,"api/passenger/book").hasAuthority(Permissions.BOOK_TICKET.name());
                    authConfig.requestMatchers(HttpMethod.POST,"/api/passenger/cancel").hasAuthority(Permissions.CANCEL_TICKET.name());
                    authConfig.requestMatchers(HttpMethod.GET,"api/passenger/viewAllTheTrainsByRegion").hasAuthority(Permissions.GET_ALL_TRAINS.name());

                    authConfig.anyRequest().denyAll();
                });
        return http.build();
    }
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };
}