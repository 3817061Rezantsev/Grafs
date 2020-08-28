package graphs;

import java.util.*;

public class GraphIterator<T> implements Iterator {
	protected T currentNode;
	protected Graph<T> graph;
	protected Stack<T> stack;
	protected HashSet<T> tree;
	public GraphIterator(Graph<T> graph, T currentNode) {
		this.graph = graph;
		this.currentNode = currentNode;
		stack = new Stack<T>();
		tree = new HashSet<T>();
		tree.add(currentNode);
		stack.add(currentNode);
	}
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		while(!stack.isEmpty()) {
			for (Edge<T> element : graph.GetEdges()) {
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
			if (!stack.isEmpty() && graph.colored(stack.peek(), tree)) {
				currentNode = stack.pop();
				return true;
			}
		}
		return false;
	}

	@Override
	public Object next() {
		// TODO Auto-generated method stub
		return currentNode;
	}

}
