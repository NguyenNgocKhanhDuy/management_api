package com.nnkd.managementbe.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduleTime {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleTime.class, args);
    }
}
