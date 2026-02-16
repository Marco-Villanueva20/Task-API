package dev.marco.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskApiApplication {

	public static void main(String[] args) {
		Character c = 56;
		System.out.println(c);
		SpringApplication.run(TaskApiApplication.class, args);
	}

}
