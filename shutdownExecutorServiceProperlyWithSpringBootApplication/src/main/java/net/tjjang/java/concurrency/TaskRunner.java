package net.tjjang.java.concurrency;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.String.format;

/**
 * Created by colin on 15. 8. 1..
 */
@Service
public class TaskRunner {
	private ExecutorService executor;
	private CompletionService<Boolean> completionService;
	private int numberOfThread;

	public TaskRunner(int n) {
		numberOfThread = n;
	}

	@PostConstruct
	public void initialize() {
		System.out.println("Initialize taskRunner");
		executor = Executors.newFixedThreadPool(numberOfThread);
		completionService = new ExecutorCompletionService<Boolean>(executor);
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Clear-up taskRunner");
		if (executor == null)
			return;
		if (executor.isShutdown())
			return;

		List<Runnable> neverCommenced = executor.shutdownNow();
		System.out.println(format("Never commenced tasks:%s",neverCommenced));
		try {
			executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException ignore) {
		}
	}

	public void process(int count) {
		List<Future<Boolean>> futures = new ArrayList<>(count);
		System.out.println("-> Start to submit");
		int submitted = 0;
		while (--count > 0) {
			submitted++;
			Future<Boolean> queued = completionService.submit(new Callable<Boolean>() {
				@Override public Boolean call() throws Exception {
					System.out.println(format("-> a task(%s) on %s", this, Thread.currentThread().getName()));
					Thread.sleep(5000);
					System.out.println(format("<- a task(%s) on %s", this, Thread.currentThread().getName()));
					return true;
				}
			});
			futures.add(queued);
		}
		System.out.println(format("<- Submit completed : %s.", submitted));

		System.out.println("-> Waits for the tasks to be completed");
		Random random = new Random();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception ignore) {
			}
			if (!random.nextBoolean()) {
				System.out.println("<- Stop waiting");
				break;
			}
		}

	}
}