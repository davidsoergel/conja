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
@Deprecated
abstract class RunnableForEach<I> extends MappingIterator<I, Runnable>
	{
	protected RunnableForEach(final Iterator<I> i)
		{
		super(i);
		}

	protected RunnableForEach(final Iterable<I> it)
		{
		super(it);
		}

	protected RunnableForEach(final ThreadSafeNextOnlyIterator<I> i)
		{
		super(new ThreadSafeNextOnlyIteratorAsNormalIterator<I>(i));
		}

	public Runnable function(final I i)
		{
		return new Runnable()
		{
		public void run()
			{
			performAction(i);
			}
		};
		}

	public abstract void performAction(final I i);
	}
