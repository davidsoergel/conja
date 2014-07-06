/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

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
