package com.project.team4backend.global.security.Config;


import com.project.team4backend.global.security.JwtUtil;
import com.project.team4backend.global.security.filter.CustomLoginFilter;
import com.project.team4backend.global.security.filter.ForcePasswordChangeFilter;
import com.project.team4backend.global.security.filter.JwtAuthorizationFilter;
import com.project.team4backend.global.security.handler.CustomLogoutHandler;
import com.project.team4backend.global.security.handler.CustomLogoutSuccessHandler;
import com.project.team4backend.global.security.handler.JwtAccessDeniedHandler;
import com.project.team4backend.global.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 빈 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final RedisTemplate<String, String> redisTemplate;


    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/v1/auths/login", //로그인 은 인증이 필요하지 않음
            "/api/v1/auths/signup", // 회원가입은 인증이 필요하지 않음
            "/api/v1/auths/login/kakao",
            "/api/v1/auths/reset-temp-password",
            "/api/v1/auths/reissue", // 토큰 재발급은 인증이 필요하지 않음
            "/api/v1/auths/emailVerifications/send",
            "/api/v1/auths/emailVerifications/check",
            //"/auth/**",
            "/api/usage",
            "/swagger-ui/**",   // swagger 관련 URL
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        CustomLoginFilter loginFilter = new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/auths/login");

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(allowUrl).permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new ForcePasswordChangeFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .logout(logout -> logout.logoutUrl("/members/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}