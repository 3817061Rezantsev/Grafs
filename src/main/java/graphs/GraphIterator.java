package graphs;

import java.util.*;

public class GraphIterator<T> implements Iterator<T> {
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
		if (!stack.isEmpty() && graph.colored(stack.peek(), tree)) {
			currentNode = stack.pop();
			return true;
		}
		if (!stack.isEmpty()) {
			DeepDive();
			currentNode = stack.pop();
			return true;
		} else return false;
		/*while (!stack.isEmpty()) {
			if (!stack.isEmpty() && graph.colored(stack.peek(), tree)) {
				currentNode = stack.pop();
				return true;
			}
			for (Edge<T> element : graph.GetEdges()) {
				if (element.first.equals(stack.peek()) && tree.add(element.last)) {
					stack.add(element.last);
				}
				if (element.last.equals(stack.peek()) && element.flag && tree.add(element.first)) {
					stack.add(element.first);
				}
			}

		}*/
		
	}

	private void DeepDive() {
		for (Edge<T> element : graph.GetEdges()) {
			if (element.first.equals(stack.peek()) && tree.add(element.last)) {
				stack.add(element.last);
			}
			if (element.last.equals(stack.peek()) && element.flag && tree.add(element.first)) {
				stack.add(element.first);
			}
		}
		if (!stack.isEmpty() && !graph.colored(stack.peek(), tree)) {
			DeepDive();
		}

	}

	@Override
	public T next() {
		return currentNode;
	}

}
