package com.xiong.reggiewaimai.config;

import com.xiong.reggiewaimai.common.JacksonObjectMapper;
import com.xiong.reggiewaimai.common.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author xsy
 * @date 2022/8/9
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        LoginCheckFilter loginCheckFilter=new LoginCheckFilter();
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(loginCheckFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor();
//        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**")
//                .excludePathPatterns("/backend/api/**","/backend/images/**","/backend/js/**","/backend/plugins/**","/backend/styles/**","/backend/favicon.ico");
//    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
    }
}
