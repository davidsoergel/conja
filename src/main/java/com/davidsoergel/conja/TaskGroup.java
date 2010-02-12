package com.davidsoergel.conja;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
class TaskGroup extends MappingIterator<Runnable, ComparableFutureTask> //implements Iterator<FutureTask>  // could be
	{
	private final Set<ComparableFutureTask> futuresEnqueued = new HashSet<ComparableFutureTask>();

	private final Set<ComparableFutureTask> futuresDoneAwaitingResultCollection = new HashSet<ComparableFutureTask>();

	public static final ThreadLocal<int[]> _currentTaskPriority = new ThreadLocal<int[]>();

	private int[] currentTaskPriority;
	private int subPriority = 0;  // start the first task with the best priority

	private final Semaphore outstandingTasks;

	protected TaskGroup(final Iterator<Runnable> taskIterator, int queueSize) //, int[] currentTaskPriority)
		{
		super(taskIterator);
		this.currentTaskPriority = _currentTaskPriority.get();
		outstandingTasks = new Semaphore(queueSize);
		}

	/**
	 * Check whether all jobs have completed, whether or not they have had their results collected already
	 *
	 * @return
	 */
	public synchronized boolean isDone()
		{
		synchronized (futuresEnqueued)
			{
			return futuresEnqueued.isEmpty() && !super.hasNext();
			}
		}

	public void blockUntilDone() throws ExecutionException, InterruptedException
		{
		assert !super.hasNext();

		// slightly weird to avoid ConcurrentModificationException
		while (!futuresEnqueued.isEmpty())
			{
			FutureTask future;
			synchronized (futuresEnqueued)
				{
				Iterator<ComparableFutureTask> it = futuresEnqueued.iterator();
				if (it.hasNext())
					{
					future = it.next();
					}
				else
					{
					future = null;
					}
				}
			if (future != null)
				{
				future.get();  // this removes the task from futuresEnqueued, via reportDone, so we'll get a different one the next time around

				if (!futuresEnqueued.isEmpty())
					{
					// give the tasks some time to finish without churning this polling thread
					// PERF task granularity trouble; we waste time here
					Thread.sleep(10);
					}
				}
/*
			Iterator<ComparableFutureTask> futuresEnqueuedIterator = futuresEnqueued.iterator();
			while (futuresEnqueuedIterator.hasNext())
				{
				FutureTask future = futuresEnqueuedIterator.next();
				future.get();

				// ** liable to produce ConcurrentModificationException?

				// reportDone deals with these
				//	futuresEnqueuedIterator.remove();
				//	assert futuresDoneAwaitingResultCollection.remove(future);
				}*/
			}
		}

	/**
	 * Block until all tasks are done, throwing the first exception encountered, if any.
	 *
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public synchronized void getAllExceptions() throws ExecutionException, InterruptedException
		{
		assert isDone();

		synchronized (futuresDoneAwaitingResultCollection)
			{
			Iterator<ComparableFutureTask> futuresDoneIterator = futuresDoneAwaitingResultCollection.iterator();
			while (futuresDoneIterator.hasNext())
				{
				FutureTask future = futuresDoneIterator.next();
				future.get();
				futuresDoneIterator.remove();
				}
			}
		}

	/**
	 * Rewritten from ArrayUtils.add for package independence
	 *
	 * @param currentTaskPriority
	 * @param subPriority
	 * @return
	 */
	private static int[] arrayAppend(final int[] currentTaskPriority, final int subPriority)
		{
		int[] result;
		if (currentTaskPriority == null)
			{
			result = new int[]{subPriority};
			}
		else
			{
			result = Arrays.copyOf(currentTaskPriority, currentTaskPriority.length + 1);
			result[currentTaskPriority.length] = subPriority;
			}
		return result;
		}

	public ComparableFutureTask function(final Runnable task)
		{
		synchronized (futuresEnqueued)
			{
			// then start each successive task with a worse priority
			int[] s = arrayAppend(currentTaskPriority, subPriority);
			ComparableFutureTask ftask = new ComparableFutureTask(task, s, this);
			futuresEnqueued.add(ftask);
			subPriority++;
			return ftask;
			}
		}

	public ComparableFutureTask next()
		{
		if (!hasNext())
			{
			return null;
			}
		// wait for a permit
		outstandingTasks.acquireUninterruptibly();
		return super.next();
/*		Runnable task = taskIterator.next();
		// then start each successive task with a worse priority
		subPriority++;
		int[] s = DSArrayUtils.add(currentTaskPriority, subPriority);
		FutureTask ftask = new ComparableFutureTask(task, s);
		futures.add(ftask);
		return ftask;*/
		}
/*
	public void remove()
		{
		throw new NotImplementedException();
		}*/


	public ComparableFutureTask nextIfPermitAvailable()
		{
		if (hasPermits())
			{
			return next();
			}
		return null;
		}

	public boolean hasPermits()
		{
		return outstandingTasks.availablePermits() > 0;
		}

	public void reportDone(final ComparableFutureTask task)
		{
		synchronized (futuresEnqueued)
			{
			if (!futuresEnqueued.remove(task))
				{
				throw new ThreadingException("Can't report a task complete on the wrong TaskGroup");
				}
			synchronized (futuresDoneAwaitingResultCollection)
				{
				futuresDoneAwaitingResultCollection.add(task);
				}
			outstandingTasks.release();
			}
		}

	boolean aborted = false;

	@Override
	public boolean hasNext()
		{
		return !aborted && super.hasNext();
		}

	public void shutdownNow()
		{
		aborted = true;
		outstandingTasks.drainPermits();
		}
	}
