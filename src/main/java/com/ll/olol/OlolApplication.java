package com.ll.olol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // @EntityListeners(AuditingEntityListener.class) 가 작동하도록 허용
@EnableScheduling //스케쥴링 사용 시 추가
public class OlolApplication {

    public static void main(String[] args) {
        SpringApplication.run(OlolApplication.class, args);
    }
}
