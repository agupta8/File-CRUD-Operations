package com.example.FileOperations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class FileOperationsApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FileOperationsApplication.class, args);
    }

    @Bean
    public Docket swaggerConfiguration()
    {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/app/*"))
            .apis(RequestHandlerSelectors.basePackage("com.example.FileOperations.controller")).build();
    }

}
