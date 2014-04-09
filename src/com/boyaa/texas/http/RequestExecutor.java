package com.boyaa.texas.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;


public class RequestExecutor {
	private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread thread =  new Thread(r, "HttpRquest task #" + mCount.getAndIncrement());
			thread.setPriority(5);
			return thread;
		}
	};
	
	private static ResponsePoster mPoster = new ResponsePoster(new Handler(Looper.getMainLooper()));

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
			KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
	
	private static Dialog dialog = new LoadingDialog(App.instance);
	
	public static void execute(Request<?> request, boolean showProgressBar) {
		request.dialog = dialog;
		HttpTask task = new HttpTask(request, mPoster);
//		if (showProgressBar)
//			dialog.show();
		THREAD_POOL_EXECUTOR.execute(task);
	}
}