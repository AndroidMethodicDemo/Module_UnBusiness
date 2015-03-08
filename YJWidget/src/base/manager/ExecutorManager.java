package base.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class ExecutorManager {

	public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";

	private static ThreadPoolExecutor mLongPool = null;
	private static Object mLongLock = new Object();

	private static ThreadPoolExecutor mShortPool = null;
	private static Object mShortLock = new Object();

	private static ThreadPoolExecutor mDownloadPool = null;
	private static Object mDownloadLock = new Object();

	private static Map<String, ThreadPoolExecutor> mMap = new HashMap<String, ThreadPoolExecutor>();
	private static Object mSingleLock = new Object();
	
	/** 获取下载线程 */
	public static ThreadPoolExecutor getDownloadPool() {
		synchronized (mDownloadLock) {
			if (mDownloadPool == null||mDownloadPool.isShutdown()) {
				mDownloadPool = getPool(3, 3, 5L);
			}
			return mDownloadPool;
		}
	}

	/** 获取一个用于执行长耗时任务的线程池，避免和短耗时任务处在同一个队列而阻塞了重要的短耗时任务，通常用来联网操作 */
	public static ThreadPoolExecutor getLongPool() {
		synchronized (mLongLock) {
			if (mLongPool == null||mLongPool.isShutdown()) {
				mLongPool = getPool(5, 5, 5L);
			}
			return mLongPool;
		}
	}

	/** 获取一个用于执行短耗时任务的线程池，避免因为和耗时长的任务处在同一个队列而长时间得不到执行，通常用来执行本地的IO/SQL */
	public static ThreadPoolExecutor getShortPool() {
		synchronized (mShortLock) {
			if (mShortPool == null||mShortPool.isShutdown()) {
				mShortPool = getPool(4, 6, 5L);
			}
			return mShortPool;
		}
	}

	/** 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题 */
	public static ThreadPoolExecutor getSinglePool() {
		return getSinglePool(DEFAULT_SINGLE_POOL_NAME);
	}

	/** 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题 */
	public static ThreadPoolExecutor getSinglePool(String name) {
		synchronized (mSingleLock) {
			ThreadPoolExecutor singlePool = mMap.get(name);
			if (singlePool == null) {
				singlePool = getPool(1, 1, 5L);
				mMap.put(name, singlePool);
			}
			return singlePool;
		}
	}
	
	private static ThreadPoolExecutor getPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new AbortPolicy());
	}
	
	
	/** 取消线程池中某个还未执行的任务 */
	public synchronized void cancel(Runnable run,ThreadPoolExecutor mPool) {
		if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
			mPool.getQueue().remove(run);
		}
	}

	/** 取消线程池中某个还未执行的任务 */
	public synchronized boolean contains(Runnable run,ThreadPoolExecutor mPool) {
		if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
			return mPool.getQueue().contains(run);
		} else {
			return false;
		}
	}

	/** 立刻关闭线程池，并且正在执行的任务也将会被中断 */
	public void stop(ThreadPoolExecutor mPool) {
		if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
			mPool.shutdownNow();
		}
	}

	/** 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭 */
	public synchronized void shutdown(ThreadPoolExecutor mPool) {
		if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
			mPool.shutdownNow();
		}
	}

}
