package backend.sculptor.global.config;

import backend.sculptor.global.auth.handler.CustomLogoutSuccessHandler;
import backend.sculptor.global.auth.handler.CustomAuthenticationSuccessHandler;
import backend.sculptor.global.auth.service.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final OAuth2MemberService oAuth2MemberService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessHandler(customLogoutSuccessHandler))
                .authorizeRequests()
            .requestMatchers("/private/**").authenticated() //private로 시작하는 uri는 로그인 필수
                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //admin으로 시작하는 uri는 관릴자 계정만 접근 가능
            .anyRequest().permitAll() //나머지 uri는 모든 접근 허용
                .and().oauth2Login((oauth2) -> oauth2
                        .successHandler(customAuthenticationSuccessHandler)
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2MemberService))
                        .loginPage("/loginForm") //로그인이 필요한데 로그인을 하지 않았다면 이동할 uri 설정
                        //.defaultSuccessUrl("/", true)
                );

        return httpSecurity.build();
    }
}