package checker.util;


public class Tuple<X,Y> {

	private final X x;
	
	private final Y y;
	
	public Tuple(X x, Y y) {
		if(x == null || y == null) {
			throw new IllegalArgumentException("Cannot have null elements in a tuple");
		}
		this.x = x;
		this.y = y;
	}

	public X getX() {
		return x;
	}

	public Y getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x.hashCode();
		result = prime * result + y.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tuple))
			return false;
		final Tuple<?,?> other = (Tuple) obj;
		if (!x.equals(other.x))
			return false;
		if (!y.equals(other.y))
			return false;
		return true;
	}
	
}
