package com.joeyliao.linknote.generic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class appConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(
//            "http://linknote.online",
//            "https://linknote.online",
//            "http://192.168.0.201",
//            "https://192.168.0.201",
//            "http://192.168.0.203",
//            "https://192.168.0.203",
//            "http://172.17.0.2"
            "*"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*");
  }
}
