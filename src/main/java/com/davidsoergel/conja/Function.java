package com.davidsoergel.conja;

/**
 * Copied from com.google.common.base.Function to avoid the package dependency.  This is so minor that I'm not worried
 * about the license.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */


public interface Function<F, T>
	{

	/**
	 * Applies the function to an object of type {@code F}, resulting in an object of type {@code T}.  Note that types
	 * {@code F} and {@code T} may or may not be the same.
	 *
	 * @param from the source object
	 * @return the resulting object
	 */
	T apply(F from);

	/**
	 * Indicates whether some other object is equal to this {@code Function}. This method can return {@code true}
	 * <i>only</i> if the specified object is also a {@code Function} and, for every input object {@code o}, it returns
	 * exactly the same value.  Thus, {@code function1.equals(function2)} implies that either {@code function1.apply(o)}
	 * and {@code function2.apply(o)} are both null, or {@code function1.apply(o).equals(function2.apply(o))}.
	 * <p/>
	 * <p>Note that it is always safe <em>not</em> to override {@link Object#equals}.
	 */
	boolean equals(Object obj);
	}
