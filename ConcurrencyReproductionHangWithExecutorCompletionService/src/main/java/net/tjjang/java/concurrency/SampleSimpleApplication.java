package net.tjjang.java.concurrency;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class SampleSimpleApplication implements CommandLineRunner {

	@Autowired
	HangReproducer reproducer;

	@Override
	public void run(String... args) {
		System.out.println("start reproduce");
		reproducer.reproduce(100);
		System.out.println("finish reproduce");
	}

	public static void main(String[] args) throws Exception {
		System.out.println("start run");
		ConfigurableApplicationContext ctx = SpringApplication.run(SampleSimpleApplication.class, args);
		System.out.println("end run");
		SpringApplication.exit(ctx, new ExitCodeGenerator() {
			@Override public int getExitCode() {
				return 0;
			}
		});
	}

}