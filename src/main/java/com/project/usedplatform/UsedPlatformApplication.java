package com.project.usedplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // 하위 패키지 모두 스캔됨
public class UsedPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsedPlatformApplication.class, args);
	}
}