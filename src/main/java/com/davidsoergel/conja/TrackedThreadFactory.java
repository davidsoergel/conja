package com.davidsoergel.conja;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The default thread factory
 */
class TrackedThreadFactory implements ThreadFactory
	{
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String namePrefix;

	List<Long> createdThreadIds = new ArrayList<Long>();

	TrackedThreadFactory()
		{
		SecurityManager s = System.getSecurityManager();
		ThreadGroup parentGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		String poolName = "pool-" + poolNumber.getAndIncrement();
		group = new ThreadGroup(parentGroup, poolName);
		namePrefix = poolName + "-thread-";

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		bean.setThreadContentionMonitoringEnabled(true);
		bean.setThreadCpuTimeEnabled(true);
		}

	public Thread newThread(Runnable r)
		{
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		// ** I have no idea why I previously wanted to ensure that these threads are user threads.
/*		if (t.isDaemon())
			{
			t.setDaemon(false);
			}*/
		// ** clearly they should be daemon threads!
		t.setDaemon(true);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			{
			t.setPriority(Thread.NORM_PRIORITY);
			}
		createdThreadIds.add(t.getId());
		return t;
		}

	public ThreadPoolPerformanceStats getStats()
		{
		return new ThreadPoolPerformanceStats(createdThreadIds);
		}
	}
