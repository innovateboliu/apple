package delay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TweetGenerator {
	
	private static final double DISTANCE = 120000;
	private static final double TIME = 120;
	
	private static final double COEFFICIENT = 0.75;
	
	private Schedule[] schedules;
	
	private List<Tweet> tweets;
	
	private Calendar curCalendar;
	
	private Calendar stopCalendar;
	
	private Location Tonka, Timbuktu;
	
	private Random[] delayRandoms;
	
	public TweetGenerator() {
		Tonka = new Location("Tonka", 0, 0);
		Timbuktu = new Location("Timbuktu", 10, 0);
		schedules = new Schedule[4];
		schedules[0] = new Schedule(Timbuktu, Tonka, 0, 2, 1);
		schedules[1] = new Schedule(Tonka, Timbuktu, 6, 8, -1);
		schedules[2] = new Schedule(Timbuktu, Tonka, 12, 14, 1);
		schedules[3] = new Schedule(Tonka, Timbuktu, 18, 20, -1);
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
				double speed = DISTANCE / TIME; 
				generateTweetsToAlign(schedule, speed);
				break;
			}
		}
		
		while (curCalendar.before(stopCalendar)) {
			Direction direction = curHour / 6 %2 == 0 ? Direction.TIMBUKTUTOTONKA : Direction.TONKATOTIMBUKTU;
			if (curHour % 6 > 2) { // the train is at station
				double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Tonka.getLongitude() : Timbuktu.getLongitude();
				double curLat = Timbuktu.getLongitude();
				generateTweet(curLong, curLat);
				curCalendar.add(Calendar.MINUTE, 5);
			} else { // the train is on the way or delay at station
				int delayMinutes = 0;
				if (direction.equals(Direction.TIMBUKTUTOTONKA)) { // from Timbuktu to Tonka
					delayMinutes = 30 + (int)delayRandoms[1].nextGaussian() * 10;
				} else {  // from Tonka to Timbuktu
					delayMinutes = 15 + (int)delayRandoms[0].nextGaussian() * 5;
				}
				generateTweetsWhenRunning(delayMinutes, direction);
			}
		}
	}
	
	private void generateTweetsWhenRunning(int delayMinutes, Direction direction) {
		int curMinute = curCalendar.get(Calendar.MINUTE);
		while (curMinute < delayMinutes) {
			double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Tonka.getLongitude() : Timbuktu.getLongitude();
			double curLat = Timbuktu.getLongitude();
			generateTweet(curLong, curLat);
			curCalendar.add(Calendar.MINUTE, 5);
		}
		
		double speed = DISTANCE / (TIME - (1 * COEFFICIENT) * delayMinutes);
		Schedule schedule = schedules[curCalendar.get(Calendar.HOUR_OF_DAY) / 6];
		generateTweetsToAlign(schedule, speed);
	}
	
	private void generateTweetsToAlign(Schedule schedule, double speed) {
		for (int curHour = curCalendar.get(Calendar.HOUR_OF_DAY); curHour < schedule.getDepartureHour(); curCalendar.add(Calendar.MINUTE, 5)) {
			int curMinute = curCalendar.get(Calendar.MINUTE);
			
			double curLong = (curMinute + 60 * (curHour - schedule.getDepartureHour())) * speed * schedule.getDirection() + schedule.getFrom().getLongitude();
			double curLat = schedule.getFrom().getLongitude();
			generateTweet(curLong, curLat);
		}
	}
	
	private void generateTweet(double curLong, double curLat) {
		double noiseLong = 1;
		tweets.add(new Tweet("I have seen the time machine!", curLong + noiseLong, curLat, curCalendar));
	}

	public static void main(String[] args) {
		TweetGenerator ins = new TweetGenerator();
		ins.generateTweets(1394596846, 1000);
		
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
