package location;

public class Utils {
	
	public static double longitudeToX(double longitude, double avgLong) {
		double standardParallel = Math.cos(avgLong);
		return longitude * standardParallel;
	}
	
	public static double latitudeToY(double latitude) {
		return latitude;
	}
	
	public static double getDistance(double rssi, double rssiOneMeter) {
		return Math.pow(10, (rssi - rssiOneMeter) / (-10 * 2));
	}
	
	public static double xToLongitude(double x, double avgLong) {
		double standardParallel = Math.cos(avgLong);
		return x / standardParallel;
	}
	
	public static double yToLatitude(double y) {
		return y;
	}
}
