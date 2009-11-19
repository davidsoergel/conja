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
