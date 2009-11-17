package com.davidsoergel.conja;

import com.google.common.base.Function;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class Parallel
	{
// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(Parallel.class);


// -------------------------- STATIC METHODS --------------------------

	public static <T> void forEach(NextOnlyIterator<T> tasks, final Function<T, Void> function)
		{
		forEach(new NextOnlyIteratorAsNormalIterator<T>(tasks), function);
		}

	public static <T> void forEach(Iterator<T> tasks, final Function<T, Void> function)
		{
		DepthFirstThreadPoolExecutor.getInstance().submitAndWaitForAll(new ForEach<T>(tasks)
		{
		public void performAction(final T o)
			{
			function.apply(o);
			}
		});
		}

	public static <T> void forEach(int repetitions, final Function<Integer, Void> function)
		{
		DepthFirstThreadPoolExecutor.getInstance()
				.submitAndWaitForAll(new ForEach<Integer>(new IntegerIterator(0, repetitions))
				{
				public void performAction(final Integer o)
					{
					function.apply(o);
					}
				});
		}

	public static <T> void forEachThread(final Function<Integer, Void> function)
		{
		int threads = DepthFirstThreadPoolExecutor.getInstance().getPoolSize();
		forEach(threads, function);
		}

	public static void emergencyAbort() //OutOfMemoryError e)
		{
		DepthFirstThreadPoolExecutor.getInstance().shutdownNow();
		//throw e;
		}

	public static <T> void forEach(Iterable<T> tasks, final Function<T, Void> function)
		{
		forEach(tasks.iterator(), function);
		}

	public static <T, V> Map<T, V> map(NextOnlyIterator<T> tasks, final Function<T, V> function)
		{
		return map(new NextOnlyIteratorAsNormalIterator<T>(tasks), function);
		}

	public static <T, V> Map<T, V> map(Iterator<T> tasks, final Function<T, V> function)
		{
		final Map<T, V> result = new ConcurrentHashMap<T, V>();
		DepthFirstThreadPoolExecutor.getInstance().submitAndWaitForAll(new ForEach<T>(tasks)
		{
		public void performAction(final T o)
			{
			result.put(o, function.apply(o));
			}
		});
		return result;
		}

	public static <T, V> Map<T, V> map(Iterable<T> tasks, final Function<T, V> function)
		{
		return map(tasks.iterator(), function);
		}


	public static void shutdown()
		{
		logger.warn("Shutting down Parallel thread pool");
		DepthFirstThreadPoolExecutor.getInstance().shutdown();
		}

// -------------------------- INNER CLASSES --------------------------

	// unlike RunnableForEach, this one puts the Iterator.next() call within each Runnable
	// it will iterate forever until one of the Runnables throws an exception, so it's important to throttle elsewhere, e.g. via the permits in DepthFirstTPE.

	private abstract static class ForEach<T> implements Iterator<Runnable>
		{
// ------------------------------ FIELDS ------------------------------

		Iterator<T> iter;

		boolean hasNext = true;


// --------------------------- CONSTRUCTORS ---------------------------

		public ForEach(final Iterator<T> tasks)
			{
			iter = tasks;
			}

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Iterator ---------------------

		public boolean hasNext()
			{
			return hasNext;
			}

		public Runnable next()
			{
			return new Runnable()
			{
			public void run()
				{
				T o;
				try
					{
					o = iter.next();
					}
				catch (NoSuchElementException e)
					{
					// happens all the time due to concurrency, no problem
					hasNext = false;
					return;
					}
				catch (OutOfMemoryError e)
					{
					emergencyAbort(); //e);
					throw e;
					}

				try
					{
					performAction(
							o); // exceptions are thrown from DepthFirstThneadPoolIterator wrapped in RuntimeExecutionException and ExecutionException
					}
				catch (OutOfMemoryError e)
					{
					emergencyAbort(); //e);
					throw e;
					}
				/*			}
			   catch (Throwable e)
				   {
				   logger.error("Error", e);
				   }*/
				}
			};
			}

		public void remove()
			{
			}

// -------------------------- OTHER METHODS --------------------------

		public abstract void performAction(T o);
		}
	}
