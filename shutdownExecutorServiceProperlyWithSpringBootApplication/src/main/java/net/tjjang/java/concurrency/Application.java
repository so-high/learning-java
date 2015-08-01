package net.tjjang.java.concurrency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	TaskRunner taskRunnerService;

	@Override
	public void run(String... args) {
		System.out.println("start service");
		taskRunnerService.process(100);
		System.out.println("end service");
	}

	public static void main(String[] args) throws Exception {
		System.out.println("start application");
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		System.out.println("end application");
		SpringApplication.exit(ctx, new ExitCodeGenerator() {
			@Override public int getExitCode() {
				return 0;
			}
		});
	}

}