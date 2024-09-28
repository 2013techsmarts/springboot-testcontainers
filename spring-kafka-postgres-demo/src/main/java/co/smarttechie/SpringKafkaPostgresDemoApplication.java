package co.smarttechie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Pattern;

@SpringBootApplication
public class SpringKafkaPostgresDemoApplication {

	public static void main(String[] args) {
		String regex = "^/env/.*$"; // Example: only alphanumeric characters and spaces
		System.out.println(Pattern.matches(regex, "/env"));
		SpringApplication.run(SpringKafkaPostgresDemoApplication.class, args);
	}

}
