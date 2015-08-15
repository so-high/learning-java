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

	public TaskRunner() {
		this(5);
	}

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
		System.out.println("Start to clear taskRunner");
		if (executor == null)
			return;
		if (executor.isShutdown())
			return;

		executor.shutdownNow();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}
		}
		System.out.println("Cleared all taskRunner");
	}

	public void process(int count) {
		List<Future<Boolean>> futures = new ArrayList<>(count);
		System.out.println("Start process");
		int submitted = 0;
		while (--count > 0) {
			submitted++;
			Future<Boolean> queued = completionService.submit(new Callable<Boolean>() {
				@Override public Boolean call(){
					System.out.println(format("->-> a task(%s) on %s", this, Thread.currentThread().getName()));
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						System.out.println(format("<-<- a task(%s) has got interrupted", this));
						return false;
					}
					System.out.println(format("<-<- a task(%s) has been done on %s", this));
					return true;
				}
			});
			futures.add(queued);
		}
		System.out.println(format("Submit completed : %s.", submitted));
		System.out.println("-> Waits for the tasks to be completed");
		Random random = new Random();
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception ignore) {
			}
			System.out.println(".");
			if (random.nextInt()%20==0) {
				System.out.println("<- Stop waiting, stop sign arrived.");
				break;
			}
		}
		System.out.println("End process");
	}
}