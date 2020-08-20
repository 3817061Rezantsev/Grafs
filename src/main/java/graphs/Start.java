package graphs;

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
		gr.addEdge(1, 9);
		gr.show();
		gr.startDFS(1);
		gr.search(12, 1);
		gr.addVertex(13);
		gr.addDirectedEdge(12, 13);
		gr.search(1, 13);
		gr.search(13, 1);
	}
}
