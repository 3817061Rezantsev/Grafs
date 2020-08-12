package Grafs;

public class Edge<T> {
	public T first;
	public T last;
	Edge(T first, T last) {
		this.first = first;
		this.last = last;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)first + (int)last;
		result = prime * result;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
            return false;
		}
		@SuppressWarnings("unchecked")
		Edge<T> edge = (Edge<T>) obj;
		if((this.first == edge.first)&&(this.last == edge.last)||(this.first == edge.last)&&(this.last == edge.first))
			return true;
		else return false;
	}
	public void Show() {
		System.out.print("( " + first+", " + last + ")");
	}
}
