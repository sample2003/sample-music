package com.sample.music.config;

import com.sample.music.interceptors.AdminInterceptor;
import com.sample.music.interceptors.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
//@EnableWebMvc // 避免覆盖SpringBoot默认配置
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AdminInterceptor adminInterceptor;

    /**
     * 放行前端接口访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("http://localhost:8088") // 允许指定域名访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的头信息
                .allowCredentials(true); // 允许认证信息
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        // 原有排除路径保留
                        "/api/**/select/**",
                        "/api/user/login",
                        "/api/user/register",
                        "/api/user/sendVerifyCode",

                        // 补充Knife4j相关路径
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",  // 新增OpenAPI规范路径
                        "/webjars/**",
                        "/doc.html",
                        "/doc.html/**",     // 增加二级路径匹配
                        "/favicon.ico"      // 网站图标
                );


        registry
                .addInterceptor(adminInterceptor)
                .addPathPatterns("/api/admin/**")  // 精确限定管理接口路径
                .excludePathPatterns(
                        "/api/admin/login",
                        "/api/admin/logout",
                        // 排除Knife4j接口
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 禁用默认静态资源处理
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        }

        // Knife4j文档资源
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        // WebJars资源
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // OpenAPI规范文件
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/v3/api-docs/");
    }
}
