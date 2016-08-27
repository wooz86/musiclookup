package com.wooz86.musiclookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MusiclookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusiclookupApplication.class, args);
	}
}


