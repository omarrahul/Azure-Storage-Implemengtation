package com.example.AzureBlobStorageImplementation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AzureBlobStorageImplementationApplication implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

	}

	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(AzureBlobStorageImplementationApplication.class, args);
	}
}
