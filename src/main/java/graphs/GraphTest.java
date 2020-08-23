package graphs;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class GraphTest {

	@Test
	public void test() throws InterruptedException {
		Graph<Integer> graph = new Graph<Integer>();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);

		final int numOfReadThreads = 10;
		CountDownLatch latch = new CountDownLatch(numOfReadThreads);
		List<SearchGraphThread> threads = new ArrayList<>();
		for (int i = 0; i < numOfReadThreads; i++) {
			threads.add(new SearchGraphThread(graph, latch));
		}
		Executor executor = Executors.newFixedThreadPool(threads.size());
		for (Runnable runnable : threads) {
			executor.execute(runnable);
		}
		latch.await(10, TimeUnit.SECONDS);

		for (SearchGraphThread graphThread : threads) {
			assertTrue("Thread should exit succesfully", graphThread.isSuccess());
		}
	}

	private static class SearchGraphThread implements Runnable {
		private Graph<Integer> graph;
		protected CountDownLatch latch;
		private boolean success;

		public SearchGraphThread(Graph<Integer> graph, CountDownLatch latch) {
			this.graph = graph;
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				List<Integer> path = graph.search(1, 3);
				Assert.assertArrayEquals("Search result check", new Integer[] { 1, 2, 3 }, path.toArray());
				setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

	}
}
