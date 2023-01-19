package life.zengc.community.community.intercepters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
// 增加EnableMvc注解，由于其中有注解若WebMvcConfigurationSupport存在，则不走WebMvcConfigurer，导致静态资源请求被拦截
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Autowired
    private RquestInterceptor rquestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(rquestInterceptor).addPathPatterns("/**");
    }
}
