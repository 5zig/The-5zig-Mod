package eu.the5zig.mod.manager;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Scheduler {

	private final Queue<Task> jobs = Queues.newArrayDeque();

	@EventHandler
	public void onTick(TickEvent event) {
		synchronized (jobs) {
			for (int i = 0; i < jobs.size(); i++) {
				Task task = jobs.peek();
				if (++task.tickCount >= task.delay) {
					try {
						((FutureTask) task.listenableFuture).run();
					} catch (Throwable e) {
						The5zigMod.logger.error("Could not execute task", e);
					}
					jobs.remove(task);
				}
			}
		}
	}

	/**
	 * Posts a runnable to the Main Server Thread.
	 * <p/>
	 *
	 * @param runnable The runnable that should be executed.
	 * @return a Listenable Future.
	 */
	public ListenableFuture postToMainThread(Runnable runnable) {
		return postToMainThread(runnable, false);
	}

	/**
	 * Posts a runnable to the Main Server Thread.
	 * <p/>
	 *
	 * @param runnable The runnable that should be executed.
	 * @return a Listenable Future.
	 */
	public ListenableFuture postToMainThread(Runnable runnable, boolean noThreadCheck) {
		return postToMainThread(runnable, noThreadCheck, 0);
	}

	/**
	 * Posts a runnable to the Main Server Thread.
	 * <p/>
	 *
	 * @param runnable The runnable that should be executed.
	 * @return a Listenable Future.
	 */
	public ListenableFuture postToMainThread(Runnable runnable, boolean noThreadCheck, int delay) {
		Callable callable = Executors.callable(runnable);
		ListenableFuture listenableFuture = ListenableFutureTask.create(callable);
		if (noThreadCheck || delay > 0 || !The5zigMod.getVars().isMainThread()) {
			synchronized (jobs) {
				jobs.add(new Task(listenableFuture, delay));
				return listenableFuture;
			}
		} else {
			try {
				return Futures.immediateFuture(callable.call());
			} catch (Exception e) {
				return Futures.immediateFailedCheckedFuture(e);
			}
		}
	}

	private class Task {

		private ListenableFuture listenableFuture;
		private int delay;
		private int tickCount;

		public Task(ListenableFuture listenableFuture, int delay) {
			this.listenableFuture = listenableFuture;
			this.delay = delay;
		}
	}
}
