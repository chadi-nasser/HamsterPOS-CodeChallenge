package dev.chadinasser.hamsterpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HamsterPOSApplication {

    public static void main(String[] args) {
        SpringApplication.run(HamsterPOSApplication.class, args);
    }

}
