package edu.gdpu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PunchApplication.class, args);
    }

}
