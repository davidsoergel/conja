/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.conja;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator that maps elements from an underlying iterator through some function on the fly.  Easily extended as an
 * anonymous class.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public abstract class MappingThreadSafeNextOnlyIterator<T, J> implements ThreadSafeNextOnlyIterator<J>
	{
	ThreadSafeNextOnlyIterator<T> i;

	public MappingThreadSafeNextOnlyIterator(ThreadSafeNextOnlyIterator<T> i)
		{
		this.i = i;
		}

	public MappingThreadSafeNextOnlyIterator(Iterator<T> i)
		{
		this.i = new IteratorAsThreadSafeNextOnlyIterator(i);
		}

	public J next() throws NoSuchElementException
		{
		return function(i.next());
		}

	public abstract J function(T t);
	}
