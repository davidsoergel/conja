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
