package backend.sculptor.global.config;

import backend.sculptor.global.oauth.handler.CustomLogoutSuccessHandler;
import backend.sculptor.global.oauth.handler.CustomAuthenticationSuccessHandler;
import backend.sculptor.global.oauth.service.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@ComponentScan(basePackages = "backend")
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
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/token/**").permitAll() //토큰 발급을 위한 경로는 모두 허용
                        .requestMatchers("/loginForm","/","/css/**","/images/**","/js/**","/favicon.ico").permitAll()
                        .requestMatchers("/loginForm").authenticated() // 로그인 접근 가능
                                .anyRequest().permitAll()
                        )
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            //.requestMatchers("/private/**").authenticated() //private로 시작하는 uri는 로그인 필수
                //.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //admin으로 시작하는 uri는 관릴자 계정만 접근 가능
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oAuth2MemberService))
                                .successHandler(customAuthenticationSuccessHandler)
                                //.loginPage("/loginForm") //로그인이 필요한데 로그인을 하지 않았다면 이동할 uri 설정
                        //.defaultSuccessUrl("/", true)
                );

        return httpSecurity.build();
    }
}