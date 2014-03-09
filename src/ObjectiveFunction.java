import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;

public class ObjectiveFunction implements
		DifferentiableMultivariateVectorFunction, Serializable {

	private static final long serialVersionUID = 7072187082052755854L;
	private List<Double> x;
	private List<Double> y;
	private List<Double> distance;

	public ObjectiveFunction() {
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		distance = new ArrayList<Double>();
	}

	public void addPoint(double x, double y, double z) {
		this.x.add(x);
		this.y.add(y);
		this.distance.add(z);
	}

	public double[] calculateTarget() {
		double[] target = new double[distance.size()];
		for (int i = 0; i < distance.size(); i++) {
			target[i] = distance.get(i).doubleValue();
		}
		return target;
	}

	private double[][] jacobian(double[] variables) {
		double[][] jacobian = new double[x.size()][2];
		for (int i = 0; i < jacobian.length; ++i) {
			jacobian[i][0] = 2 * variables[0] - 2 * x.get(i);
			jacobian[i][1] = 2 * variables[1] - 2 * y.get(i);
		}
		return jacobian;
	}

	public double[] value(double[] variables) {
		double[] values = new double[x.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = Math.pow(variables[0] - x.get(i), 2) + Math.pow(variables[1] - y.get(i), 2);
		}
		return values;
	}

	public MultivariateMatrixFunction jacobian() {
		return new MultivariateMatrixFunction() {
			private static final long serialVersionUID = -8673650298627399464L;

			public double[][] value(double[] point) {
				return jacobian(point);
			}
		};
	}
}