package delay.estimation;

public class Tuple<T> {
	private T a;
	private T b;
	
	public T getA() {
		return a;
	}

	public void setA(T a) {
		this.a = a;
	}

	public T getB() {
		return b;
	}

	public void setB(T b) {
		this.b = b;
	}

	public Tuple() {
	}
	
	public Tuple(T a, T b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public Tuple<T> clone() {
		Tuple<T> result = new Tuple<T>();
		result.setA(a);
		result.setB(b);
		return result;
	}
}
