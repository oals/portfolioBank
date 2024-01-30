package com.project.bank.global.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Log4j2
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        
        //클라이언트에서 url을 "/images"로 시작하는 경로일 경우
        // uploadpath에 설정한 폴더를 기준으로 파일을 업로드하도록 설정


        registry.addResourceHandler("/images/**") //리소스 읽어내기
                .addResourceLocations(uploadPath);

    }
}


/*
    업로드 파일을 읽어올 경로 설정
    WebMvcConfigurer 인터페이스를 구현하여 
    addResourcehandler메서드를 통해 로컬 컴퓨터에 업로드한 파일을 찾을 위치를 설정
    
    


 */