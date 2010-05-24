package com.davidsoergel.conja;

import java.util.ArrayList;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class TestShutdown
	{
	public static void main(String[] argv)
		{

		ArrayList<String> x = new ArrayList<String>();
		x.add("A");
		x.add("B");
		x.add("C");

		Parallel.forEach(x, new Function<String, Void>()
		{
		public Void apply(String obj)
			{
			System.out.println(obj);
			return null;
			}
		});

		System.out.print("Done!");
		}
	}
