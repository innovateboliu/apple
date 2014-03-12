package delay_estimation;

public class Utils {
	public static final double DISTANCE = 120000;
	public static final double TIME = 120;
	public static final double COEFFICIENT = 0.75; // arrivalDelay = COEFFICIENT * departureDelay;
	public static final Location Tonka = new Location("Tonka", 1.08, 0);
	public static final Location Timbuktu = new Location("Timbuktu", 0, 0);
	public static Schedule[] buildSchedules() {
		
		Schedule[] schedules = new Schedule[4];
		schedules[0] = new Schedule(Timbuktu, Tonka, 0, 2, 0);
		schedules[1] = new Schedule(Tonka, Timbuktu, 6, 8, 180);
		schedules[2] = new Schedule(Timbuktu, Tonka, 12, 14, 0);
		schedules[3] = new Schedule(Tonka, Timbuktu, 18, 20, 180);
		return schedules;
	}
	
	public static double calcDistance(double fromLong, double fromLat, double toLong, double toLat) {
		double earthRadius = 3958.75;
	    double dLat = Math.toRadians(toLat-fromLat);
	    double dLng = Math.toRadians(toLong-fromLong);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    final int meterConversion = 1609;

	    return dist * meterConversion;
	}
	
	public static Tuple<Double> calcCoordinates(final double distance, final Tuple<Double> origpoint, final double angle) {
        final double distanceNorth = Math.sin(Math.toRadians(angle)) * distance;
        final double distanceEast = Math.cos(Math.toRadians(angle)) * distance;
 
        final double earthRadius = 6371000;
        
        final double newLat = origpoint.getB() + (distanceNorth / earthRadius) * 180 / Math.PI;

        final double newLon = origpoint.getA() + (distanceEast / (earthRadius * Math.cos(newLat * 180 / Math.PI))) * 180 / Math.PI;
 
        return new Tuple<Double>(newLon, newLat);
	}
	
	public static class ExecutionOrderException extends Exception {
		private static final long serialVersionUID = 3319839166913989186L;
		public ExecutionOrderException(String message) {
			super(message);
		}
	}

}	
