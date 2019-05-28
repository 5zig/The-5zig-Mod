package eu.the5zig.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class that allows runnables to be executed in a seperate Thread.
 */
public class AsyncExecutor {

	private ExecutorService service;

	public AsyncExecutor() {
		service = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Async Executor Pool #%1$d").build());
	}

	/**
	 * Executes a runnable in a single async thread. Multiple requests in a row may get queued.
	 *
	 * @param runnable the runnable that should be executed asynchronously.
	 */
	public void execute(Runnable runnable) {
		service.execute(runnable);
	}

	/**
	 * Shuts the async executor service down.
	 */
	public void finish() {
		service.shutdown();
	}

}
