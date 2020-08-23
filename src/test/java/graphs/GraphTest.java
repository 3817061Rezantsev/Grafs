package graphs;

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
	}

	//@Test
	public void concurrencyTest() throws InterruptedException {
		Graph<Integer> graph = new Graph<Integer>();
		Integer v1 = 1;
		Integer v2 = 100;
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);

		final int numOfReadThreads = 10;
		CountDownLatch latch = new CountDownLatch(numOfReadThreads);
		List<GraphThread> threads = new ArrayList<>();
		for (int i = 0; i < numOfReadThreads; i++) {
			threads.add(new SearchGraphThread(graph, v1, v2, latch));
		}
		Executor executor = Executors.newFixedThreadPool(threads.size());
		for (Runnable runnable : threads) {
			executor.execute(runnable);
		}
		latch.await(10, TimeUnit.SECONDS);

		for (GraphThread graphThread : threads) {
			assertTrue("Thread should exit succesfully", graphThread.isSuccess());
		}
	}

	private static abstract class GraphThread implements Runnable {
		protected Graph<Integer> graph;
		protected Integer v1;
		protected Integer v2;
		protected CountDownLatch latch;
		private boolean success;

		public GraphThread(Graph<Integer> graph, Integer v1, Integer v2, CountDownLatch latch) {
			this.graph = graph;
			this.v1 = v1;
			this.v2 = v2;
			this.latch = latch;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}

	private static class SearchGraphThread extends GraphThread {

		public SearchGraphThread(Graph<Integer> graph, Integer v1, Integer v2, CountDownLatch latch) {
			super(graph, v1, v2, latch);
		}

		@Override
		public void run() {
			try {
				List<Integer> path = null;
				while (path == null || path.isEmpty()) {
					path = graph.search(v1, v2);
				}
				List<Integer> expectedPath = IntStream.rangeClosed(v1, v2).boxed().collect(Collectors.toList());
				Assert.assertArrayEquals("Search result check", expectedPath.toArray(), path.toArray());
				setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}

	}
}
