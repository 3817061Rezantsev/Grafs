package Grafs;
import java.util.*;

public class Graf <T> {
	public int size;
	public HashSet<T> Vertex;
	public HashSet<Edge<T>> Edges;
	public HashSet<T> tree;
	public ArrayList<T> list;
	public Deque<T> queue;
	Graf() {
		size = 0;
		Vertex = new HashSet<T>();
		Edges = new HashSet<Edge<T>>();
		tree = new HashSet<T>();
		list = new ArrayList<T>();
		queue = new LinkedList<T>();
	}
	public void Show() {
		for (T element : Vertex) {
			System.out.print(element.toString() + ", ");
		}
		System.out.println();
		for (Edge<T> element : Edges) {
			element.Show();
			System.out.println();
		}
	}
	public void AddVertex(T ver) {
		Vertex.add(ver);
		size++;
	}
	public void AddEdge(Edge<T> e) {
		boolean f = false, l = false;
		for (T element : Vertex) {
			if(element == e.first)
				f = true;
			if(element == e.last)
				l = true;
		}
		if (f && l)
			Edges.add(e);
		else
			System.out.println("Таких вершин не существует");
	}
	public void AddEdge(T first, T last) {
		Edge<T> e = new Edge<T>(first, last);
		this.AddEdge(e);
	}
	public void DFS(T start) {
		tree.add(start);
		list.add(start);
		for(Edge<T> element : Edges) {
			if (element.first == start) {
				boolean f = tree.add(element.last);
				if (f) {
					DFS(element.last);
				}
			}
			if (element.last == start) {
				boolean f = tree.add(element.first);
				if (f) {
					DFS(element.first);
				}
			}	
		}
	}
	public void StartDFS(T start) {
		tree.clear();
		list.clear();
		DFS(start);
		for(T element : list)
			System.out.print(element.toString() + ", ");
		System.out.println();
	}
	public void DaWay(T start, T finish) {
		tree.add(start);
		queue.add(start);
		if ((start != finish)&&(!tree.contains(finish))) {
			while(Colored(queue.getLast()))
				queue.pollLast();
			boolean f = false;
			for(Edge<T> element : Edges) {
			if ((element.first == start)) {
				f = tree.add(element.last);
				if (f) {
					DaWay(element.last, finish);
				}
			}
			if ((element.last == start)) {
				f = tree.add(element.first);
				if (f) {
					DaWay(element.first, finish);
				}
			}	
			}
		}
		
	}
	public void Search(T start, T finish) {
		tree.clear();
		while(!queue.isEmpty()) {
			queue.poll();
		}
		DaWay(start, finish);
		while(queue.getLast() != finish) {
			queue.pollLast();
		}
		while(!queue.isEmpty()) {
			System.out.print(queue.poll() + ", ");
		}
		System.out.println();
	}
	public boolean Colored(T ver) {
		boolean f = true;
		for (Edge<T> element : Edges) {
			if (element.first == ver) {
				f = tree.contains(element.last);
				if (!f) {
					break;
				}
			}
			if (element.last == ver) {
				f = tree.contains(element.first);
				if (!f) {
					break;
				}
			}	
		}
		return f;
	}
}
