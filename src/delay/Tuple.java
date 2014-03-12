package delay;

public class Tuple {
	private double a;
	private double b;
	
	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public Tuple() {
	}
	
	public Tuple(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public Tuple clone() {
		Tuple result = new Tuple();
		result.setA(a);
		result.setB(b);
		return result;
	}
}
