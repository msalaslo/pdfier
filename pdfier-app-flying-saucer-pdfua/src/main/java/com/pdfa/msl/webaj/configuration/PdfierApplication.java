package com.pdfa.msl.webaj.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan ("com")
public class PdfierApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfierApplication.class, args);
	}

}
