package com.library.LibraryApp;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.web.filter.LoggingFilter;
import io.r2dbc.postgresql.*;
import io.r2dbc.postgresql.codec.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.server.WebFilter;

@SpringBootApplication
@OpenAPIDefinition
public class LibraryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryAppApplication.class, args);
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public WebFilter loggingFilter() {
		return new LoggingFilter();
	}




}

