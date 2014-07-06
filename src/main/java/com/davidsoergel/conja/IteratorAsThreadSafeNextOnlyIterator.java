/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

//import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Ensures that every next() call is synchronized.  It is important for the underlying iterator not to be modified (some
 * will throw ConcurrentModificationException in this case, but that's not guaranteed).
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class IteratorAsThreadSafeNextOnlyIterator<T> implements ThreadSafeNextOnlyIterator<T>
	{
	Iterator<T> iter;

	public IteratorAsThreadSafeNextOnlyIterator(final Iterator<T> iter)
		{
		this.iter = iter;
		}

//	@NotNull

	public T next() throws NoSuchElementException
		{
		synchronized (iter)
			{
			return iter.next();
			}
		}
	}
