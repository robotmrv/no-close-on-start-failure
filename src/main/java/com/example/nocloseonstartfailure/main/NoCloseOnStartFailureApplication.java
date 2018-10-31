package com.example.nocloseonstartfailure.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.io.Closeable;
import java.io.IOException;

@SpringBootApplication
public class NoCloseOnStartFailureApplication {
	public static final Logger LOG = LoggerFactory.getLogger(NoCloseOnStartFailureApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(NoCloseOnStartFailureApplication.class);
		app.addListeners(new ApplicationListener<ApplicationFailedEvent>() {
			//close context bootstrap on Load Failure
			//see similar issue to https://github.com/spring-cloud/spring-cloud-commons/issues/333
			//https://github.com/spring-cloud/spring-cloud-commons/commit/5802c0e2be792738d86509e3d2c93dcd8b997d4d#diff-344ead166eff93b62c827e6f8ff1caedR172
			@Override
			public void onApplicationEvent(ApplicationFailedEvent event) {
				if (true) return;//fixme uncomment to apply WA
				ApplicationContext context = event.getApplicationContext().getParent();
				while (context instanceof Closeable) {
					try {
						((Closeable) context).close();
					}
					catch (IOException e) {
						LOG.error("Cannot close context: " + context.getId(), e);
					}
					context = context.getParent();
				}
			}
		});
		app.run(args);
	}

	@Bean
	public Object badBean() {
		throw new RuntimeException("boom");
	}
}
