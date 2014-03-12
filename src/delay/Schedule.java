package delay;

public class Schedule {
	private int departureHour;
	private int arrivalHour;
	private Location from;
	private Location to;
	private int direction;
	
	public Schedule(Location from, Location to, int departureHour,
			int arrivalHour, int direction) {
		super();
		this.from = from;
		this.to = to;
		this.departureHour = departureHour;
		this.arrivalHour = arrivalHour;
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public int getArrivalHour() {
		return arrivalHour;
	}

	public void setArrivalHour(int arrivalHour) {
		this.arrivalHour = arrivalHour;
	}

	public int getDepartureHour() {
		return departureHour;
	}

	public void setDepartureHour(int departureHour) {
		this.departureHour = departureHour;
	}
}