/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class ThreadUtils
	{
	public static void checkInterrupted() throws InterruptedException
		{
		if (Thread.interrupted())
			{
			throw new InterruptedException();
			}
		}
	}
