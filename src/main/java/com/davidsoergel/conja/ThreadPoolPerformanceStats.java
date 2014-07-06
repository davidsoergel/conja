/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class ThreadPoolPerformanceStats
	{
	long cpuTime;
	long userTime;
	//long systemTime;
	long waitedTime;
	long blockedTime;

	public ThreadPoolPerformanceStats(Collection<Long> ids)
		{
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		if (bean.isThreadCpuTimeSupported())
			{
			for (long id : ids)
				{
				long tc = bean.getThreadCpuTime(id);
				if (tc != -1)
					{
					cpuTime += tc;
					}
				long tu = bean.getThreadUserTime(id);
				if (tu != -1)
					{
					userTime += tu;
					}
				}
			}
		for (long id : ids)
			{
			ThreadInfo ti = bean.getThreadInfo(id);
			blockedTime += ti.getBlockedTime();
			waitedTime += ti.getWaitedTime();
			}
		}

	public double getCpuSeconds()
		{
		return cpuTime / (double) 1000000000;
		}

	public double getUserSeconds()
		{
		return userTime / (double) 1000000000;
		}

	public double getSystemSeconds()
		{
		return (cpuTime - userTime) / (double) 1000000000;
		}

	public double getBlockedSeconds()
		{
		return blockedTime / (double) 1000;
		}

	public double getWaitedSeconds()
		{
		return waitedTime / (double) 1000;
		}

	@Override
	public String toString()
		{
		return "ThreadPoolPerformanceStats{" + "blockedTime=" + blockedTime + ", cpuTime=" + cpuTime + ", userTime="
		       + userTime + ", waitedTime=" + waitedTime + '}';
		}
	}
