package com.example.nocloseonstartfailure.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;

@EnableScheduling
@Configuration
public class MyBootstrapConfig {

    @Bean
    ThreadPoolTaskScheduler myDisposableBean() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setDaemon(false);
        return scheduler;
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("MyBootstrapConfig.preDestroy()");
    }

    @PostConstruct
    public void addTask() {
        ThreadPoolTaskScheduler scheduler = myDisposableBean();
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("ping");
        }, Duration.ofMillis(1000));
    }
}
