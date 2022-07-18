package com.face.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加放行的路径
        String[] addPath = {"/**"};
        String[] excludePath = {"/face/vef",
                "/js/**","/img/**","/*.ico","/*.css"
        };
        registry.addInterceptor(new FaceConfig()).addPathPatterns(addPath).excludePathPatterns(excludePath);
    }
}
