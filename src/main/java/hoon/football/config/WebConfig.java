package hoon.football.config;

import hoon.football.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns( // 로그인이 필요없는 페이지
                        "/",
                        "/login",
                        "/members/new",
                        "/members",
                        "/teams",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico",
                        "/error",

                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                        );
    }

}
