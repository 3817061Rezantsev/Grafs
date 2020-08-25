package graphs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Graph<T> {
	protected HashSet<T> vertexes;
	protected Set<Edge<T>> edges;

	public Graph() {
		vertexes = new HashSet<T>();
		edges = Collections.newSetFromMap(new ConcurrentHashMap<>());
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
			if (element.equals(e.GetFirst()))
				f = true;
			if (element.equals(e.GetLast()))
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
/*
	private void dfs(T start, ArrayList<T> list) {
		tree.add(start);
		list.add(start);
		for (Edge<T> element : edges) {
			if (element.first == start) {
				boolean f = tree.add(element.last);
				if (f) {
					dfs(element.last, list);
				}
			}
			if (element.last == start) {
				boolean f = tree.add(element.first);
				if (f) {
					dfs(element.first, list);
				}
			}
		}
	}

	public ArrayList<T> startDFS(T start) {
		tree.clear();
		ArrayList<T> list = new ArrayList<T>();
		dfs(start, list);
		return list;
	}
*/
	private void daWay(T start, T finish, Deque<T> queue, HashSet<T> tree) {
		tree.add(start);
		queue.add(start);
		if ((start != finish) && (!tree.contains(finish))) {
			while (!queue.isEmpty() && colored(queue.getLast(), tree))
				queue.pollLast();
			boolean f = false;
			for (Edge<T> element : edges) {
				if (element.first.equals(start)) {
					f = tree.add(element.last);
					if (f) {
						daWay(element.last, finish, queue, tree);
					}
				}
				if (element.last.equals(start) && element.flag) {
					f = tree.add(element.first);
					if (f) {
						daWay(element.first, finish, queue, tree);
					}
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> search(T start, T finish) {
		HashSet<T> tree = new HashSet<T>();
		Deque<T> queue = new LinkedList<T>();
		if (edges.isEmpty()) {
			return (List<T>) queue;
		}
		daWay(start, finish, queue, tree);
		while (!queue.isEmpty() && (!queue.getLast().equals(finish))) {
			queue.pollLast();
		}
		return (List<T>) queue;
	}
	private boolean colored(T ver, HashSet<T> tree) {
		boolean f = true;
		for (Edge<T> element : edges) {
			if (element.first.equals(ver)) {
				f = tree.contains(element.last);
				if (!f) {
					break;
				}
			}
			if (element.last.equals(ver)) {
				f = tree.contains(element.first);
				if (!f) {
					break;
				}
			}
		}
		return f;
	}
}
