/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Deprecated
public class ProgressReportingThreadPoolExecutor extends ThreadPoolExecutor
	{
	private static final Logger logger = Logger.getLogger(ProgressReportingThreadPoolExecutor.class);

	public ProgressReportingThreadPoolExecutor(int threads, int queueSize)
		{/*
		if (threads == 0)
			{
			threads = Runtime.getRuntime().availableProcessors();
			}
		if (queueSize == 0)
			{
			queueSize = threads * 2;
			}*/
		super(threads == 0 ? Runtime.getRuntime().availableProcessors() : threads,
		      queueSize == 0 ? Runtime.getRuntime().availableProcessors() * 2 : queueSize, 0L, TimeUnit.MILLISECONDS,
		      new LinkedBlockingQueue<Runnable>());
		}

	public ProgressReportingThreadPoolExecutor()
		{
		this(0, 0);
		}

	//Collection<Future> futures = new HashSet<Future>();

	public void finish(String s, int reportEveryNSeconds)
		{
		shutdown();

		// then report progress every 30 seconds
		while (!isTerminated())
			{
			try
				{
				awaitTermination(reportEveryNSeconds, TimeUnit.SECONDS);
				}
			catch (InterruptedException e)
				{
				// no problem, just cycle
				}

			logger.debug(String.format(s, getCompletedTaskCount()));
			}
		logger.debug(String.format(s, getCompletedTaskCount()));
		}

	/*@Override
	public Future<?> submit(Runnable task)
		{
		Future<?> fut = super.submit(task);
		futures.add(fut);
		return fut;
		}*/
	}
