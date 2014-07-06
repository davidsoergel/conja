/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class IntegerIterator implements Iterator<Integer>, ThreadSafeNextOnlyIterator<Integer>
	{
	int trav;
	final int maxExclusive;

	public IntegerIterator(final int minInclusive, final int maxExclusive)
		{
		trav = minInclusive;
		this.maxExclusive = maxExclusive;
		}

	public synchronized boolean hasNext()
		{
		return trav < maxExclusive;
		}

	public synchronized Integer next()
		{
		if (!hasNext())
			{
			throw new NoSuchElementException();
			}
		final int result = trav;
		trav++;
		return result;
		}

	public synchronized void remove()
		{
		throw new UnsupportedOperationException();
		}
	}
