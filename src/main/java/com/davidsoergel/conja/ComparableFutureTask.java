/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask>
	{
	int[] priority;
	TaskGroup tg;

	public ComparableFutureTask(Callable<T> tCallable, int[] priority, TaskGroup tg)
		{
		super(tCallable);
		this.priority = priority;
		this.tg = tg;
		}

	public ComparableFutureTask(Runnable tCallable, int[] priority, TaskGroup tg)
		{
		super(tCallable, null);
		this.priority = priority;
		this.tg = tg;
		}

	/**
	 * Tree-structured comparison
	 *
	 * @param o
	 * @return
	 */
	public int compareTo(ComparableFutureTask o)
		{
		// always do more detailed tasks first

		int level = priority.length;
		int olevel = o.priority.length;

		if (level > olevel)
			{
			return -1;
			}
		else if (olevel < level)
			{
			return 1;
			}
		else
			{
			for (int i = 0; i < level; i++)
				{
				if (priority[i] < o.priority[i])
					{
					return -1;
					}
				else if (priority[i] > o.priority[i])
					{
					return 1;
					}
				}

			return 0;
			}
		}

	/**
	 * Copied from DSArrayUtils for package independence
	 *
	 * @param x
	 * @param separator
	 * @return
	 */
	public static String asString(int[] x, String separator)
		{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < x.length; i++)
			{
			int i1 = x[i];
			sb.append(i1);
			if (i != (x.length - 1))
				{
				sb.append(separator);
				}
			}
		return sb.toString();
		}

	/**
	 * Keep track of the priority of the currently executing task on each thread.  Priorities are tree-structured: the
	 * subtasks of a given task inherit the priority of their parent, but also may have internal order.
	 */
	private static final Logger logger = Logger.getLogger(ComparableFutureTask.class);

	@Override
	public void run()
		{
		if (logger.isDebugEnabled())
			{
			logger.debug("Running " + asString(priority, ","));
			}

		// if this task spawns more by creating a child TaskGroup, it will need to know the priority

		int[] lastPriority = TaskGroup._currentTaskPriority.get();
		TaskGroup._currentTaskPriority.set(priority);

		try
			{
			super.run();
			}
		catch (Throwable e)
			{
			// who cares about the chaining; we want the ultimate cause.
			// ** Print each level explicitly for now

			Throwable c = e.getCause();
			while (c != null && c != e) // avoid infinite loop just in case
				{
				e = c;
				logger.error("Error", e);
				c = e.getCause();
				}

			if (e instanceof OutOfMemoryError)
				{
				tg.shutdownNow();
				}
			if (e instanceof RuntimeException)
				{
				throw (RuntimeException) e;
				}
			}
		TaskGroup._currentTaskPriority.set(lastPriority);

		tg.reportDone(this);
		}
/*
	@Override
	protected void done()
		{
		tg.release();
		//tg.remove(this);
		//tg.checkAllTasksCompleted();
		}*/
	}
