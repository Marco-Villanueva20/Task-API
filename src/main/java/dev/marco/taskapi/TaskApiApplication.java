package dev.marco.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
	    info = @Info(
	        title = "Task API",
	        version = "1.0.0",
	        description = "API for managing tasks",
	        contact = @Contact(
	            name = "Marco",
	            email = "marco@example.com"
	        ),
	        license = @License(
	            name = "Apache 2.0",
	            url = "http://springdoc.org"
	        )
	    )
	)


@SpringBootApplication
public class TaskApiApplication {

	public static void main(String[] args) {
		Character c = 56;
		System.out.println(c);
		SpringApplication.run(TaskApiApplication.class, args);
	}

}
