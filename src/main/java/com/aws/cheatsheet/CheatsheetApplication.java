package com.aws.cheatsheet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Slf4j
@SpringBootApplication
public class CheatsheetApplication {

	public static void main(String[] args) {
		System.out.println("AWS endpoint: " + System.getenv().get("AWS_ENDPOINT"));
		System.out.println("AWS region: " + System.getenv().get("AWS_REGION"));
		System.out.println("AWS access key: " + System.getenv().get("AWS_ACCESS_KEY"));
		System.out.println("AWS secret key: " + System.getenv().get("AWS_SECRET_KEY"));
		System.out.println("AWS bucket: " + System.getenv().get("AWS_BUCKET"));

		SpringApplication.run(CheatsheetApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(S3Client s3Client){
		return args -> {
				s3Client.createBucket(request -> request.bucket(System.getenv("AWS_BUCKET")));
		};
	}
}
