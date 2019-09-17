package com.example.todolist;

import io.swagger.annotations.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix="swagger", name="enabled", havingValue = "true")
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .apiInfo(appInfo())
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo appInfo() {
    return new ApiInfoBuilder()
        .title("TODO App API")
        .description("Api Documentation")
        .version("0.0.1")
        .termsOfServiceUrl("urn:tos")
        .contact(new Contact("", "", ""))
        .license("Apache 2.0")
        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
        .build();
  }
}
