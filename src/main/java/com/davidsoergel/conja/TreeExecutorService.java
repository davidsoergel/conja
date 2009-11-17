package com.davidsoergel.conja;

import java.util.Iterator;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface TreeExecutorService //extends ExecutorService
	{
//	<T> Collection<T> submitAndGetAll(Iterable<Callable<T>> tasks); //, String format, int intervalSeconds);

	void submitAndWaitForAll(Iterable<Runnable> tasks); //,String format,int intervalSeconds);

	void submitAndWaitForAll(Iterator<Runnable> tasks);

//	void submitTaskGroup(TaskGroup tg);

	ThreadPoolPerformanceStats shutdown();
	}
