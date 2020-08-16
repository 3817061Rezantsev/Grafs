package Grafs;

public class Start {
	public static void main(String args[]) {
		Graf<Integer> gr = new Graf<Integer>();
		for(int i = 0; i < 12; i++)
			gr.AddVertex(i + 1);
		gr.AddEdge(1, 2);
		gr.AddEdge(7, 1);
		gr.AddEdge(8, 1);
		gr.AddEdge(2, 6);
		gr.AddEdge(2, 3);
		gr.AddEdge(3, 4);
		gr.AddEdge(3, 5);
		gr.AddEdge(8, 9);
		gr.AddEdge(12, 8);
		gr.AddEdge(9, 11);
		gr.AddEdge(10, 9);
		gr.AddEdge(1, 9);
		gr.Show();
		gr.StartDFS(1);
		gr.Search(12, 1);
		gr.AddVertex(13);
		gr.AddDirectedEdge(12, 13);
		gr.Search(1, 13);
		gr.Search(13, 1);
	}
}
