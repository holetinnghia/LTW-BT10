package ltw.bt10.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final RoleInterceptor roleInterceptor;
    public WebMvcConfig(RoleInterceptor roleInterceptor){ this.roleInterceptor = roleInterceptor; }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleInterceptor).addPathPatterns("/admin/**","/user/**");
    }
}