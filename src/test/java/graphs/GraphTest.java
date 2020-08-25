package graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

public class GraphTest {

	@Test
	public void searchNotExistingPath() {
		Graph<Integer> graph = new Graph<Integer>();
		List<Integer> path = graph.search(1, 2);
		assertTrue("Should be null or empty", path == null || path.isEmpty());
		graph.addVertex(1);
		graph.addVertex(2);
		path = graph.search(1, 2);
		assertTrue("Should be null or empty", path == null || path.isEmpty());
		graph.addVertex(3);
		graph.addEdge(1, 2);
		path = graph.search(1, 3);
		assertTrue("Should be null or empty", path == null || path.isEmpty());
	}

	@Test
	public void searchExistingPath() {
		Integer v1 = 1;
		Integer v2 = 200;

		Graph<Integer> graph = new Graph<Integer>();
		for (int i = v1; i <= v2; i++) {
			graph.addVertex(i);
		}
		for (int i = v1; i < v2; i++) {
			graph.addEdge(i, i + 1);
			List<Integer> path = graph.search(v1, i + 1);
			List<Integer> expectedPath = IntStream.rangeClosed(v1, i + 1).boxed().collect(Collectors.toList());
			Assert.assertArrayEquals("Search result check", expectedPath.toArray(), path.toArray());
		}
	}

	private Graph<Integer> createGraph(Integer v1, Integer v2) {
		Graph<Integer> graph = new Graph<Integer>();
		for (int i = v1; i <= v2; i++) {
			graph.addVertex(i);
		}
		for (int i = v1; i < v2; i++) {
			graph.addEdge(i, i + 1);
		}
		return graph;
	}

	@Test
	public void capacityTest() {
		final int size = 10000;
		Graph<Integer> graph = createGraph(1, size);
		List<Integer> path = graph.search(1, size);
		Assert.assertEquals("Search result check", size, path.size());
	}

	//@Test
	public void performanceTest() {
		long start_time, create_time, search_time;
		final int size_1 = 5000;
		final int size_2 = 20000;

		start_time = System.currentTimeMillis();
		Graph<Integer> graph_1 = createGraph(1, size_1);
		create_time = System.currentTimeMillis() - start_time;
		System.out.println("create_time (" + size_1 + "): " + create_time + " ms");

		start_time = System.currentTimeMillis();
		Graph<Integer> graph_2 = createGraph(1, size_2);
		create_time = System.currentTimeMillis() - start_time;
		System.out.println("create_time (" + size_2 + "): " + create_time + " ms");

		start_time = System.currentTimeMillis();
		graph_1.search(1, size_1);
		search_time = System.currentTimeMillis() - start_time;
		System.out.println("search_time (" + size_1 + "): " + search_time + " ms");

		start_time = System.currentTimeMillis();
		graph_2.search(1, size_2);
		search_time = System.currentTimeMillis() - start_time;
		System.out.println("search_time (" + size_2 + "): " + search_time + " ms");
	}

	@Test
	public void concurrencyTest() throws InterruptedException {
		Graph<Integer> graph = new Graph<Integer>();
		Integer v1 = 1;
		Integer v2 = 1000;
		final int numOfReadThreads = 25;

		CountDownLatch latch = new CountDownLatch(numOfReadThreads + 1);
		List<GraphThread> threads = new ArrayList<>();
		threads.add(new WriteGraphThread("write-thread", graph, v1, v2, latch));
		for (int i = 0; i < numOfReadThreads; i++) {
			threads.add(new SearchGraphThread("search-thread-" + i, graph, v1, v2, latch));
		}

		Executor executor = Executors.newFixedThreadPool(threads.size());
		for (Runnable runnable : threads) {
			executor.execute(runnable);
		}

		latch.await(600, TimeUnit.SECONDS);
		// latch.await();

		for (GraphThread graphThread : threads) {
			assertTrue("Thread " + graphThread.getName() + " should exit succesfully", graphThread.isSuccess());
		}
	}

	private static abstract class GraphThread implements Runnable {
		protected String name;
		protected Graph<Integer> graph;
		protected Integer v1;
		protected Integer v2;
		protected CountDownLatch latch;
		private boolean success;

		public GraphThread(String name, Graph<Integer> graph, Integer v1, Integer v2, CountDownLatch latch) {
			this.name = name;
			this.graph = graph;
			this.v1 = v1;
			this.v2 = v2;
			this.latch = latch;
		}

		public String getName() {
			return name;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}

	private static class SearchGraphThread extends GraphThread {

		public SearchGraphThread(String name, Graph<Integer> graph, Integer v1, Integer v2, CountDownLatch latch) {
			super(name, graph, v1, v2, latch);
		}

		@Override
		public void run() {
			try {
				System.out.println("SearchGraphThread[" + name + "]: start");
				List<Integer> path = null;
				while (path == null || path.isEmpty()) {
					path = graph.search(v1, v2);
					Thread.sleep(1);
				}
				List<Integer> expectedPath = IntStream.rangeClosed(v1, v2).boxed().collect(Collectors.toList());
				Assert.assertArrayEquals("Search result check", expectedPath.toArray(), path.toArray());

				setSuccess(true);
				System.out.println("SearchGraphThread[" + name + "]: end");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}

	}

	private static class WriteGraphThread extends GraphThread {

		public WriteGraphThread(String name, Graph<Integer> graph, Integer v1, Integer v2, CountDownLatch latch) {
			super(name, graph, v1, v2, latch);
		}

		@Override
		public void run() {
			try {
				System.out.println("WriteGraphThread: start");
				for (int i = v1; i <= v2; i++) {
					graph.addVertex(i);
				}
				for (int i = v1; i < v2; i++) {
					graph.addEdge(i, i + 1);
					Thread.sleep(10);
				}

				List<Integer> path = graph.search(v1, v2);
				List<Integer> expectedPath = IntStream.rangeClosed(v1, v2).boxed().collect(Collectors.toList());
				Assert.assertArrayEquals("Search result check", expectedPath.toArray(), path.toArray());

				setSuccess(true);
				System.out.println("WriteGraphThread: end");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}
	}
}
