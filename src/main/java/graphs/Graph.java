package graphs;

import java.util.*;

public class Graph<T> {
	protected HashSet<T> vertexes;
	protected HashSet<Edge<T>> edges;
	protected HashSet<T> tree;
	protected ArrayList<T> list;
	protected Deque<T> queue;

	public Graph() {
		vertexes = new HashSet<T>();
		edges = new HashSet<Edge<T>>();
		tree = new HashSet<T>();
		list = new ArrayList<T>();
		queue = new LinkedList<T>();
	}

	public void show() {
		for (T element : vertexes) {
			System.out.print(element.toString() + ", ");
		}
		System.out.println();
		for (Edge<T> element : edges) {
			element.Show();
			System.out.println();
		}
	}

	public void addVertex(T ver) {
		vertexes.add(ver);
	}

	public void addEdge(Edge<T> e) {
		boolean f = false, l = false;
		for (T element : vertexes) {
			if (element == e.first)
				f = true;
			if (element == e.last)
				l = true;
		}
		if (f && l)
			edges.add(e);
		else
			System.out.println("Vertex not found: " + e.first + " - " + e.last);
	}

	public void addEdge(T first, T last) {
		Edge<T> e = new Edge<T>(first, last);
		this.addEdge(e);
	}

	public void addDirectedEdge(T first, T last) {
		DirectedEdge<T> e = new DirectedEdge<T>(first, last);
		this.addEdge(e);
	}

	private void dfs(T start) {
		tree.add(start);
		list.add(start);
		for (Edge<T> element : edges) {
			if (element.first == start) {
				boolean f = tree.add(element.last);
				if (f) {
					dfs(element.last);
				}
			}
			if (element.last == start) {
				boolean f = tree.add(element.first);
				if (f) {
					dfs(element.first);
				}
			}
		}
	}
	
	public void ShowList() {
		if (list.isEmpty()) {
			System.out.println("the way does not exist");
		}
		for (T element : list)
			System.out.print(element.toString() + ", ");
		System.out.println();
	}

	public ArrayList<T> startDFS(T start) {
		tree.clear();
		list.clear();
		dfs(start);
		return list;
	}

	private void daWay(T start, T finish) {
		tree.add(start);
		queue.add(start);
		if ((start != finish) && (!tree.contains(finish))) {
			while (colored(queue.getLast()))
				queue.pollLast();
			boolean f = false;
			for (Edge<T> element : edges) {
				if ((element.first == start)) {
					f = tree.add(element.last);
					if (f) {
						daWay(element.last, finish);
					}
				}
				if ((element.last == start) && element.flag) {
					f = tree.add(element.first);
					if (f) {
						daWay(element.first, finish);
					}
				}
			}
		}

	}

	public ArrayList<T> search(T start, T finish) {
		tree.clear();
		list.clear();
		while (!queue.isEmpty()) {
			queue.poll();
		}
		daWay(start, finish);
		while (!queue.isEmpty() && (queue.getLast() != finish)) {
			queue.pollLast();
		}
		while (!queue.isEmpty()) {
			list.add(queue.poll());
		}
		return list;
	}
	private boolean colored(T ver) {
		boolean f = true;
		for (Edge<T> element : edges) {
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
