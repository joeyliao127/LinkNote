package com.joeyliao.linknoteresource.generic.config;

import com.joeyliao.linknoteresource.generic.interceptor.CollaboratorInterceptor;
import com.joeyliao.linknoteresource.generic.interceptor.DefaultInterceptor;
import com.joeyliao.linknoteresource.generic.interceptor.InvitationInterceptor;
import com.joeyliao.linknoteresource.generic.interceptor.NoteInterceptor;
import com.joeyliao.linknoteresource.generic.interceptor.NotebookInterceptor;
import com.joeyliao.linknoteresource.generic.interceptor.TagInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

  @Autowired
  NotebookInterceptor notebookInterceptor;

  @Autowired
  NoteInterceptor noteInterceptor;

  @Autowired
  TagInterceptor tagInterceptor;

  @Autowired
  InvitationInterceptor invitationInterceptor;

  @Autowired
  CollaboratorInterceptor collaboratorInterceptor;

  @Autowired
  DefaultInterceptor defaultInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(tagInterceptor)
        .addPathPatterns(
            "/api/notebooks/*/notes/**/tags"
            , "/api/notebooks/*/tags");

    registry.addInterceptor(noteInterceptor)
        .addPathPatterns("/api/notebooks/*/notes/**")
        .excludePathPatterns("/api/notebooks/*/notes/**/tags");

    registry.addInterceptor(invitationInterceptor)
        .addPathPatterns("/api/notebooks/*/invitations"
            , "/api/invitations/sent-invitations"
            , "/api/invitations/received-invitations");

    registry.addInterceptor(collaboratorInterceptor)
        .addPathPatterns("/api/notebooks/*/collaborators");

    registry.addInterceptor(notebookInterceptor)
        .addPathPatterns("/api/notebooks/**", "/api/coNotebooks")
        .excludePathPatterns(
            "/api/notebooks/*/invitations",
            "/api/notebooks/*/notes/**/",
            "/api/notebooks/*/tags",
            "/api/notebooks/**/notes/**/tags",
            "/api/notebooks/*/collaborators"
        );

    registry.addInterceptor(defaultInterceptor).addPathPatterns("*")
        .excludePathPatterns("/", "notebooks", "notes");

  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(
            "http://linknote.online",
            "https://linknote.online",
            "http://192.168.0.201",
            "https://192.168.0.201",
            "http://192.168.0.203",
            "https://192.168.0.203",
            "http://172.17.0.2"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*");
  }
}
