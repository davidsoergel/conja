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
public class ThreadingException extends RuntimeException
	{

	public ThreadingException()
		{
		}

	public ThreadingException(final Throwable e)
		{
		super(e);
		}

	public ThreadingException(final String msg)
		{
		super(msg);
		}

	public ThreadingException(final String message, final Throwable cause)
		{
		super(message, cause);
		}
	}
