package com.davidsoergel.conja;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Deprecated
public class ThreadSafeNextOnlyIteratorAsNormalIterator<T> implements Iterator<T>
	{
	ThreadSafeNextOnlyIterator<T> it;

	public ThreadSafeNextOnlyIteratorAsNormalIterator(final ThreadSafeNextOnlyIterator<T> it)
		{
		this.it = it;
		}

	private boolean hasNext = true;

	public boolean hasNext()
		{
		return hasNext;
		}

	public T next()
		{
		try
			{
			return it.next();
			}
		catch (NoSuchElementException e)
			{
			hasNext = false;
			throw e;
			}
		}

	public void remove()
		{
		throw new UnsupportedOperationException();
		}
	}
