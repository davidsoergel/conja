Conja
=====

_Incredibly easy functional concurrency in Java_

The purpose of Conja is to make it trivially easy for Java programmers to take advantage of multicore processors.  It basically wraps `java.utils.concurrent` in syntactic sugar that encourages a functional style.  It provides many of the same advantages that [jsr166y](http://gee.cs.oswego.edu/dl/concurrency-interest/) does (slated for inclusion in Java 7), though it's somewhat different under the hood.  Also, Conja seems to me to be easier to use :)

What it's not
-------------

Conja is not a JVM-based functional language like [Scala](http://www.scala-lang.org/) or [Clojure](http://clojure.org/); those are cool and all, but here we just want to make concurrent computation easy and efficient in pure Java.  Also, Conja does not provide distributed computation like [Hadoop](http://hadoop.apache.org/) or [Hazelcast](http://www.hazelcast.com/); it just exploits multiple cores on one machine, with no configuration and minimal code changes.  It has some of the same goals that Grand Central Dispatch aka [libdispatch](http://libdispatch.macosforge.org/) does for C-family languages, but Conja does not depend on kernel support or native libraries.  Also, where GCD and some other concurrency libraries use FIFO queues to schedule tasks, Conja can substantially reduce the memory requirements of your program through a prioritized task scheduling approach.

Overview
--------

### Concurrent for-each

To parallelize your computation, just replace instances of for loops: 

```java
Iterable<T> someCollection = ...

for(T obj : someCollection) {
	doSomething(obj);
	}
```
	
with the `Parallel.forEach()` construct:

```java
Iterable<T> someCollection = ...

Parallel.forEach(someCollection, new Function<T, Void>() {
	void apply(T obj) {
		doSomething(obj);
		}
	});
```


As with any concurrent computation, the individual tasks should ideally have no side effects; if you follow this functional style then you don't ever need to think about thread synchronization, because the `Parallel` construct deals with it for you.  If you do want to change some external state from within each task, just make sure that the updates are thread-safe (e.g. if you're adding items to a `Set` use a `ConcurrentSkipListSet` rather than a regular `HashSet`).  It's OK to reference variables from the surrounding scope, but they must of course be `final`


### Mapping through a function

The cleanest way to store an output from each call is to use `Parallel.map()`:

```java
Map<T, V> results = Parallel.map(someCollection, new Function<T, V>(){
	V apply(T obj) {
		V result = doSomething(obj);
		return result;
		}
	});
```

### Iterate over anything

These constructs work on `Iterator`s or `Iterable`s (e.g. any `Collection`); there's also a variant that just repeats a task a given number of times, without providing an argument.

### Nested concurrent calls work fine

What if the `doSomething()` method itself performs a `Parallel.forEach()` somewhere inside it?  That's no problem at all, and is in fact encouraged.  Conja handles nested concurrency gracefully; the same worker threads are used on each call, so there is no proliferation of threads.  Conja internally prioritizes tasks to be executed based on depth-first ordering of the entire call tree, so that subtasks contributing to an earlier high-level task always preempt later high-level tasks and their subtasks.  Basically this means that it tries to finish one whole batch of tasks before starting (or continuing) on the next batch.  In fact, Conja does not even ''instantiate'' tasks (e.g., by calling `next()` on the task iterator) until near their execution time.  In combination these strategies help to conserve memory (or conversely, they help avoid out-of-memory errors for large computations).  (See [wiki:PrinciplesOfOperation Principles of Operation] for more details.)


Documentation
-------------

 * [API docs](http://davidsoergel.github.io/conja/)
 * [Principles of Operation](wiki:PrinciplesOfOperation)

Download
--------
[Maven](http://maven.apache.org/) is the easiest way to make use of conja.  Just add these to your pom.xml:

```xml
<repositories>
	<repository>
		<id>dev.davidsoergel.com releases</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/releases</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>dev.davidsoergel.com snapshots</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/snapshots</url>
		<releases>
			<enabled>false</enabled>
		</releases>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.davidsoergel</groupId>
		<artifactId>conja</artifactId>
		<version>1.051</version>
	</dependency>
</dependencies>
```

If you really want just the jar, you can get the [latest release](http://dev.davidsoergel.com/nexus/content/repositories/releases/com/davidsoergel/conja/) from the Maven repo; or get the [latest stable build](http://dev.davidsoergel.com/jenkins/job/conja/lastStableBuild/com.davidsoergel$conja/) from the build server.

The only external dependency is on log4j, so if you don't use Maven, you'll need to grab that manually if it's not already in your classpath.
