package com.davidsoergel.conja;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class RuntimeExecutionException extends ChainedRuntimeException
	{
	public RuntimeExecutionException()
		{
		}

	public RuntimeExecutionException(final Throwable e)
		{
		super(e);
		}

	public RuntimeExecutionException(final String msg)
		{
		super(msg);
		}

	public RuntimeExecutionException(final Throwable e, final String s)
		{
		super(e, s);
		}
	}
