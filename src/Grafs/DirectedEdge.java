package Grafs;

public class DirectedEdge<T> extends Edge<T> {

	DirectedEdge(T first, T last) {
		super(first, last);
		this.flag = false;
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
            return false;
		}
		@SuppressWarnings("unchecked")
		Edge<T> edge = (Edge<T>) obj;
		if((this.first == edge.first)&&(this.last == edge.last))
			return true;
		else return false;
	}
}
