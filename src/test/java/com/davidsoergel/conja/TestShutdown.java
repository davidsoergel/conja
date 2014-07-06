/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

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
