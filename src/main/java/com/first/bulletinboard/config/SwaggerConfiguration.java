package com.first.bulletinboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}

/*
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.first.bulletinboard.controller"))
                // 스웨거가 RestController를 전부 스캔을 한다.
                // basePackage => 어디를 범위로 스캔을 할 것인지 작성
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("포스트 CRUD API")
                .description("포스트 CRUD, 게시판 API입니다.")
                .version("0.8.0")
                .build();
    }

    // 완료가 되었으면 오른쪽 URL 로 접속 => http://localhost:8080/swagger-ui.html
}*/
