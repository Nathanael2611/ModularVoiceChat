package fr.nathanael2611.modularvoicechat.util;

import java.util.concurrent.*;

public class ThreadUtil {
	
	public static void execute(int count, int delay, Runnable run) {
		for (int i = 0; i < count; i++) {
			run.run();
			if (delay > 0) {
				sleep(delay);
			}
		}
	}
	
	public static void sleep(int time) {
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException ex) {
			throw new AssertionError();
		}
	}
	
	public static ThreadFactory createDaemonFactory(String name) {
		return runnable -> {
			final Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			thread.setName(name);
			return thread;
		};
	}
}
