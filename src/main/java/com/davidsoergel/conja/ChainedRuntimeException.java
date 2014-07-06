/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.conja;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This exception is used for wrapping another exception and rethrowing it as an uncaught exception.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: ChainedRuntimeException.java 269 2008-09-23 02:20:39Z soergel $
 */
public class ChainedRuntimeException extends RuntimeException
	{
	// ------------------------------ FIELDS ------------------------------

	Throwable parent;


	// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * Creates new <code>FileUploadException</code> without detail message.
	 */
	public ChainedRuntimeException()
		{
		super();
		}

	/**
	 * Constructor declaration
	 *
	 * @param e
	 */
	public ChainedRuntimeException(Throwable e)
		{
		super();
		parent = e;
		}

	/**
	 * Constructs an <code>FileUploadException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public ChainedRuntimeException(String msg)
		{
		super(msg);
		}

	/**
	 * Constructor declaration
	 *
	 * @param e
	 * @param s
	 */
	public ChainedRuntimeException(Throwable e, String s)
		{
		super(s);
		parent = e;
		}

	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Method declaration
	 *
	 * @param pw
	 */
	public void printStackTrace(PrintWriter pw)
		{
		if (parent != null)
			{
			parent.printStackTrace(pw);
			}
		super.printStackTrace(pw);
		}

	/**
	 * Method declaration
	 *
	 * @param s
	 */
	public void printStackTrace(PrintStream s)
		{
		if (parent != null)
			{
			parent.printStackTrace(s);
			}
		super.printStackTrace(s);
		}
	}
