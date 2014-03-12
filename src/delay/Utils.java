package delay;

public class Utils {
	public static final double DISTANCE = 120000;
	public static final double TIME = 120;
	public static Schedule[] buildSchedules() {
		Location Tonka = new Location("Tonka", 120000, 0);
		Location Timbuktu = new Location("Timbuktu", 0, 0);
		Schedule[] schedules = new Schedule[4];
		schedules[0] = new Schedule(Timbuktu, Tonka, 0, 2, 1);
		schedules[1] = new Schedule(Tonka, Timbuktu, 6, 8, -1);
		schedules[2] = new Schedule(Timbuktu, Tonka, 12, 14, 1);
		schedules[3] = new Schedule(Tonka, Timbuktu, 18, 20, -1);
		return schedules;
	}
	
	public static double calcDistance(double curLong, double curLat, double fromLong, double fromLat) {
		double earthRadius = 3958.75;
	    double dLat = Math.toRadians(fromLat-curLat);
	    double dLng = Math.toRadians(fromLong-curLong);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(curLat)) * Math.cos(Math.toRadians(fromLat)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    final int meterConversion = 1609;

	    return dist * meterConversion;
	}
}	
