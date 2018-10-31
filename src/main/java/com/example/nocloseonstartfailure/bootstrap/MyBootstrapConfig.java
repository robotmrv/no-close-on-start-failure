package com.example.nocloseonstartfailure.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

@EnableScheduling
@Configuration
public class MyBootstrapConfig {

    @Bean
    ThreadPoolTaskScheduler myDisposableBean() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setDaemon(false);
        scheduler.initialize();
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("ping");
        }, Duration.ofMillis(1000));

        return scheduler;
    }
}
