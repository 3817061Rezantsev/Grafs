package graphs;

import java.util.*;

public class Start {
	
	public static void main(String args[]) {
		Graph<Integer> gr = new Graph<Integer>();
		for(int i = 0; i < 12; i++)
			gr.addVertex(i + 1);
		gr.addEdge(1, 2);
		gr.addEdge(7, 1);
		gr.addEdge(8, 1);
		gr.addEdge(2, 6);
		gr.addEdge(2, 3);
		gr.addEdge(3, 4);
		gr.addEdge(3, 5);
		gr.addEdge(8, 9);
		gr.addEdge(12, 8);
		gr.addEdge(9, 11);
		gr.addEdge(10, 9);
		
		//List<Integer> q = gr.DFS(1);
		List<Integer> q = gr.search(12, 1);
		for (Integer element : q)
			System.out.print(element.toString() + ", ");
		System.out.println();
		gr.addEdge(1, 9);
		List<Integer> h = gr.search(12, 1);
		for (Integer element : h)
			System.out.print(element.toString() + ", ");
		System.out.println();
		gr.addVertex(13);
		gr.addDirectedEdge(12, 13);
		
		
		final int size = 10000;
		//Graph<Integer> graph = createGraph(1, size);
		//List<Integer> path = graph.search(1, size);
		System.out.println("AAA");
	}

	private static Graph<Integer> createGraph(Integer v1, Integer v2) {
		Graph<Integer> graph = new Graph<Integer>();
		for (int i = v1; i <= v2; i++) {
			graph.addVertex(i);
		}
		for (int i = v1; i < v2; i++) {
			graph.addEdge(i, i + 1);
		}
		return graph;
	}

}
