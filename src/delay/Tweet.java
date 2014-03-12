package delay;

import java.util.Calendar;

public class Tweet {
	private String message;
	private double longitude;
	private double latitude;
	public Tweet(String message, double longitude, double latitude,
			Calendar calendar) {
		super();
		this.message = message;
		this.longitude = longitude;
		this.latitude = latitude;
		this.calendar = calendar;
	}
	private Calendar calendar;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
}
