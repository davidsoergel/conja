package com.davidsoergel.conja;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NextOnlyIteratorAsNormalIterator<T> implements Iterator<T>
	{
	NextOnlyIterator<T> it;

	public NextOnlyIteratorAsNormalIterator(final NextOnlyIterator<T> it)
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
