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
	
	public Set<Edge<T>> GetEdges() {
		return edges;
	}

	public Iterator<T> iterator() {
		return new GraphIterator<T>(this, vertexes.iterator().next());
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

	public synchronized void addEdge(Edge<T> e) {
		boolean f = false, l = false;
		f = vertexes.contains(e.first);
		l = vertexes.contains(e.last);
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

	public List<T> startDFS(T start) {
		Stack<T> stack = new Stack<T>();
		HashSet<T> tree = new HashSet<T>();
		ArrayList<T> list = new ArrayList<T>();
		tree.add(start);
		stack.add(start);
		while(!stack.isEmpty()) {
			for (Edge<T> element : edges) {
				if (element.first.equals(stack.peek())) {
					boolean f = tree.add(element.last);
					if (f) {
						stack.add(element.last);
					}
				}
				if (element.last.equals(stack.peek()) && element.flag) {
					boolean f = tree.add(element.first);
					if (f) {
						stack.add(element.first);
					}
				}
			}
			if (!stack.isEmpty() && colored(stack.peek(), tree)) {
				list.add(stack.pop());
			}
		}
		return (List<T>)list;
	}

	public synchronized List<T> search (T start, T finish) {
		Stack<T> stack = new Stack<T>();
		HashSet<T> tree = new HashSet<T>();
		if (edges.isEmpty()) {
			return (List<T>) stack;
		}
		tree.add(start);
		stack.add(start);
		
		while(!stack.isEmpty()) {
			for (Edge<T> element : edges) {
				if (element.first.equals(stack.peek())) {
					boolean f = tree.add(element.last);
					if (f) {
						stack.add(element.last);
					}
					if (element.last.equals(finish)) {
						break;
					}
				}
				if (element.last.equals(stack.peek()) && element.flag) {
					boolean f = tree.add(element.first);
					if (f) {
						stack.add(element.first);
					}
					if (element.first.equals(finish)) {
						break;
					}
				}
				
			}
			if (!stack.isEmpty() && colored(stack.peek(), tree) && !stack.peek().equals(finish)) {
				stack.pop();
			}
			if (!stack.isEmpty() && stack.peek().equals(finish)) {
				break;
			}
		}
		return (List<T>)stack;
	}
	
	public boolean colored(T ver, HashSet<T> tree) {
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
