package com.example.cliphelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
@EnableScheduling
@EnableFeignClients
public class CliphelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(CliphelperApplication.class, args);
	}

	@PostConstruct
	public void timezone() {
		// 스프링 부트 타임존 설정
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.printf("==========설정된 서버의 타임존과 현재 시각: %s ==============\n	", LocalDateTime.now());
	}

}
