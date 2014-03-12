package location;
import java.util.Arrays;

import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

public class Positioning {
	private static ObjectiveFunction function;
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ObjectiveFunction function = new ObjectiveFunction();
//
//		function.addPoint(0, 0, 205);
//		function.addPoint(0, 10, 98);
//		function.addPoint(10, 0, 103);
//		function.addPoint(10, 9, 1.4);
//		function.addPoint(5, 5, 51.6);
//		function.addPoint(11, 11, 2.6);
//		function.addPoint(8, 10, 4.8);
//
////		LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
//		
////		GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
//		GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(0.1, 0.2, 50));
//		
//
//		
//		// final double[] weights = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
//		final double[] weights = { 1, 1, 1, 1, 1, 1, 1};
//
//		// final double[] initialSolution = {1, 1, 1};
//		final double[] initialSolution = { 140, 2500};
//
//		PointVectorValuePair optimum = optimizer.optimize(100, function,
//				function.calculateTarget(), weights, initialSolution);
//		double[] res = function.value(new double[] {10, 10});
//		System.out.println(optimizer.getEvaluations());
////		System.out.println(((SimpleVectorValueChecker)optimizer.getConvergenceChecker()).getAbsoluteThreshold());
////		System.out.println(((SimpleVectorValueChecker)optimizer.getConvergenceChecker()).getRelativeThreshold());
//
//		final double[] optimalValues = optimum.getPoint();
//
//		System.out.println("A: " + optimalValues[0]);
//		System.out.println("B: " + optimalValues[1]);
//		// System.out.println("C: " + optimalValues[2]);
//	}
	
	public static void getLocation(WifiAP[] wifiAPs, LocationBuilder locationBuilder) {
		double avgLong = (wifiAPs[0].getLocation().getLongitude() + wifiAPs[1].getLocation().getLongitude()) / 2;

		initFunction(wifiAPs, avgLong);
		computeLocation(wifiAPs, avgLong, locationBuilder);
	}
	
	private static void initFunction(WifiAP[] wifiAPs, double avgLong) {
		for (WifiAP ap : wifiAPs) {
			double x = Utils.longitudeToX(ap.getLocation().getLongitude(), avgLong);
			double y = Utils.latitudeToY(ap.getLocation().getLatitude());
			double distance = Utils.getDistance(ap.getRssi(), ap.getRssiOneMeter());
			
			function.addPoint(x, y, distance * distance);
		}
	}
	
	private static void computeLocation(WifiAP[] wifiAPs, double avgLong, LocationBuilder locationBuilder) {
		GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
		
		double[] weights = new double[wifiAPs.length];
		Arrays.fill(weights, 1);

		final double[] initialValues = getInitialValues(wifiAPs, avgLong);

		PointVectorValuePair optimum = optimizer.optimize(100, function,
				function.calculateTarget(), weights, initialValues);

		final double[] optimalValues = optimum.getPoint();
		double longitude = Utils.xToLongitude(optimalValues[0], avgLong);
		double latitude = Utils.yToLatitude(optimalValues[1]);
		Location location = new Location(longitude, latitude);
		double standardDeviation = getStandardDeviation(optimalValues[0], optimalValues[1], optimizer);
		locationBuilder.setLocation(location);
		locationBuilder.setStandardDeviation(standardDeviation);
	}
	
	private static double[] getInitialValues(WifiAP[] wifiAPs, double avgLong) {
		double[] initValues = new double[2];
		initValues[0] = Utils.longitudeToX(wifiAPs[0].getLocation().getLongitude(), avgLong);
		initValues[1] = Utils.latitudeToY(wifiAPs[0].getLocation().getLatitude());
		return initValues;
	}
	
	private static double getStandardDeviation(double x, double y, GaussNewtonOptimizer optimizer) {
		double[] values = function.value(new double[] {x, y});
		double[] targets = optimizer.getTarget();
		
		double sum = 0;
		
		for (int i = 0; i < values.length; i++) {
			sum += Math.pow(values[i] - targets[i], 2);
		}
		
		return Math.sqrt(sum / values.length);
	}

}
