package delay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TweetGenerator {
	
	
	
	private static final double COEFFICIENT = 0.75;
	
	private Schedule[] schedules;
	
	private List<Tweet> tweets;
	
	private Calendar curCalendar;
	
	private Calendar stopCalendar;
	
	private Location Tonka, Timbuktu;
	
	private Random[] delayRandoms;
	
	public TweetGenerator() {
		tweets = new ArrayList<Tweet>();
		
		Tonka = new Location("Tonka", 120000, 0);
		Timbuktu = new Location("Timbuktu", 0, 0);
		schedules = Utils.buildSchedules();
		curCalendar = Calendar.getInstance();
		stopCalendar = Calendar.getInstance();
		
		delayRandoms = new Random[2];
		delayRandoms[0] = new Random();
		delayRandoms[1] = new Random();
	}
	public void generateTweets(long startTime, long duration) {
		curCalendar.setTime(new Date(startTime));
		stopCalendar.setTime(new Date(startTime + duration));
		
		int curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
		
		for (Schedule schedule : schedules) {
			if (curHour >= schedule.getDepartureHour() && curHour < schedule.getArrivalHour()) {
				double speed = Utils.DISTANCE / Utils.TIME; 
				generateTweetsToAlign(schedule, speed, 0, 0);
				break;
			}
		}
		
		while (curCalendar.before(stopCalendar)) {
			Direction direction = curHour / 6 %2 == 0 ? Direction.TIMBUKTUTOTONKA : Direction.TONKATOTIMBUKTU;
			if (curHour % 6 >= 2) { // the train is at station
				double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Timbuktu.getLongitude() : Tonka.getLongitude();
				double curLat = Timbuktu.getLongitude();
				generateTweet(curLong, curLat);
				curCalendar.add(Calendar.MINUTE, 5);
			} else { // the train is on the way or delay at station
				int delayMinutes = 0;
				if (direction.equals(Direction.TIMBUKTUTOTONKA)) { // from Timbuktu to Tonka
					delayMinutes = (int)(30 + delayRandoms[1].nextGaussian() * 10);
				} else {  // from Tonka to Timbuktu
					delayMinutes = (int)(15 + delayRandoms[0].nextGaussian() * 5);
				}
				generateTweetsWhenRunning(delayMinutes, direction);
			}
			curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
		}
	}
	
	private void generateTweetsWhenRunning(int departureDelayMinutes, Direction direction) {
		int curMinute = curCalendar.get(Calendar.MINUTE);
		while (curMinute < departureDelayMinutes) {
			double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Tonka.getLongitude() : Timbuktu.getLongitude();
			double curLat = Timbuktu.getLongitude();
			generateTweet(curLong, curLat);
			curCalendar.add(Calendar.MINUTE, 5);
			curMinute = curCalendar.get(Calendar.MINUTE);
		}
		
		double speed = Utils.DISTANCE / (Utils.TIME - (1 - COEFFICIENT) * departureDelayMinutes);
		Schedule schedule = schedules[curCalendar.get(Calendar.HOUR_OF_DAY) / 6];
		generateTweetsToAlign(schedule, speed, departureDelayMinutes, (int)(departureDelayMinutes * COEFFICIENT));
	}
	
	private void generateTweetsToAlign(Schedule schedule, double speed, int departureDelayMinutes, int arrivalDelayMinutes) {
		int startMinutues = departureDelayMinutes;
		int stopMinutes = 120 + arrivalDelayMinutes;
		for (int curMinute = curCalendar.get(Calendar.MINUTE); curMinute <= stopMinutes; ) {
			double curLong = (curMinute - startMinutues) * speed * schedule.getDirection() + schedule.getFrom().getLongitude();
			double curLat = schedule.getFrom().getLatitude();
			generateTweet(curLong, curLat);
			
			curCalendar.add(Calendar.MINUTE, 5);
			curMinute = curCalendar.get(Calendar.MINUTE) + 60 * (curCalendar.get(Calendar.HOUR_OF_DAY) - schedule.getDepartureHour());
		}
	}
	
	private void generateTweet(double curLong, double curLat) {
		double noiseLong = 1;
		tweets.add(new Tweet("I have seen the time machine!", curLong + noiseLong, curLat, curCalendar));
	}

	public static void main(String[] args) {
		long ts = System.currentTimeMillis();
		Date date = new Date(1394596846000L);
							 
							 
		TweetGenerator ins = new TweetGenerator();
		ins.generateTweets(1394596846000L, 1000000000);
		                   
		
		Random r = new Random();
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);
		System.out.println(30 + r.nextGaussian() * 10);

	}
}
