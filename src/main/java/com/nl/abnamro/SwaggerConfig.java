package com.nl.abnamro;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(metaData())
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("com.nl.abnamro"))
                .paths(regex("/rest/users.*"))
                .build();
    }
	
	 private ApiInfo metaData() {
		/* ApiInfo apiInfo = new ApiInfo(
	                "Test API",
	                "Spring Boot REST API for Resource Tracker",
	                "1.0",
	                "Terms of service",
	                new Contact("Sandeep Verma", "http://localhost:8080", "sandeepvrma20@gmail.com"),
	               "Apache License Version 2.0",
	                "https://www.apache.org/licenses/LICENSE-2.0");
	        return apiInfo;*/
	        
	        return new ApiInfoBuilder()
	                .title("Spring Boot REST API")
	                .description("\"Spring Boot REST API for Resource Tracker\"")
	                .version("1.0.0")
	                .license("Apache License Version 2.0")
	                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
	                .contact(new Contact("Sandeep Verma", "http://localhost:8080", "sandeepvrma20@gmail.com"))
	                .build();
	        
	    }

}
