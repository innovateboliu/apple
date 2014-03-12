package delay_estimation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TweetGenerator {
	
	private Schedule[] schedules; 	//train schedules
	private List<Tweet> tweets;		//generated tweets
	private Calendar curCalendar;	//current time
	private Calendar stopCalendar;	//tweets are generated until this time
	private Random[] delayRandoms;	//for generating random delay
	
	public TweetGenerator() {
		tweets = new ArrayList<Tweet>();
		schedules = Utils.buildSchedules();
		curCalendar = Calendar.getInstance();
		stopCalendar = Calendar.getInstance();
		delayRandoms = new Random[2]; 
		delayRandoms[0] = new Random();	// for random delay at Timbuktu
		delayRandoms[1] = new Random();	// for random delay at Tonka
	}
	
	public List<Tweet> generateTweets(long startTime, long duration) {
		curCalendar.setTime(new Date(startTime));
		stopCalendar.setTime(new Date(startTime + duration));
		
		int curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
		
		// handle the special situation that the startTime is during the train running time
		for (Schedule schedule : schedules) {
			if (curHour >= schedule.getDepartureHour() && curHour < schedule.getArrivalHour()) {
				double speed = Utils.DISTANCE / Utils.TIME; 
				generateTweetsWhenRunning(schedule, speed, 0, 0);
				break;
			}
		}
		
		while (curCalendar.before(stopCalendar)) {
			Direction direction = curHour / 6 %2 == 0 ? Direction.TIMBUKTUTOTONKA : Direction.TONKATOTIMBUKTU;
			
			// the train is at station, generate tweets according to the station coordination
			if (curHour % 6 >= 2) { 
				double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Utils.Timbuktu.getLongitude() : Utils.Tonka.getLongitude();
				double curLat = Utils.Timbuktu.getLongitude();
				generateTweet(curLong, curLat);
				curCalendar.add(Calendar.MINUTE, 5);
			} else { // the train is on the way or delay at station
				int delayMinutes = 0;
				if (direction.equals(Direction.TIMBUKTUTOTONKA)) { // from Timbuktu to Tonka
					// generate random delay conform to Gaussian Distribution
					delayMinutes = (int)(30 + delayRandoms[1].nextGaussian() * 10);
				} else {  // from Tonka to Timbuktu
					// generate random delay conform to Gaussian Distribution
					delayMinutes = (int)(15 + delayRandoms[0].nextGaussian() * 5);
				}
				
				Calendar realDepartureTime = Calendar.getInstance();
				realDepartureTime.setTime((Date)curCalendar.getTime().clone());
				realDepartureTime.set(Calendar.MINUTE, delayMinutes);
				generateTweetsFromDepartureTime(realDepartureTime, direction);
			}
			curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
		}
		return tweets;
	}
	
	private void generateTweetsFromDepartureTime(Calendar realDepartureTime, Direction direction) {
		double curLong = direction.equals(Direction.TONKATOTIMBUKTU) ? Utils.Tonka.getLongitude() : Utils.Timbuktu.getLongitude();
		double curLat = Utils.Timbuktu.getLongitude();
		
		// if the train is delayed, generate tweets according to the station coordination
		while (curCalendar.before(realDepartureTime)) {
			generateTweet(curLong, curLat);
			curCalendar.add(Calendar.MINUTE, 5);
		}
		
		int departureDelayMinutes = realDepartureTime.get(Calendar.MINUTE);
		// the train is on the way, speed is calculated according to the estimated arrivalDelay which is calculated by arrivalDelay = COEFFICIENT * departureDelay
		double speed = Utils.DISTANCE / (Utils.TIME - (1 - Utils.COEFFICIENT) * departureDelayMinutes);
		Schedule schedule = schedules[curCalendar.get(Calendar.HOUR_OF_DAY) / 6];
		generateTweetsWhenRunning(schedule, speed, departureDelayMinutes, (int)(departureDelayMinutes * Utils.COEFFICIENT));
	}
	
	private void generateTweetsWhenRunning(Schedule schedule, double speed, int departureDelayMinutes, int arrivalDelayMinutes) {
		int startMinutues = departureDelayMinutes;
		int stopMinutes = 120 + arrivalDelayMinutes;
		for (int curMinute = curCalendar.get(Calendar.MINUTE); curMinute <= stopMinutes; ) {
			Tuple<Double> coor = Utils.calcCoordinates((curMinute - startMinutues) * speed, new Tuple<Double>(schedule.getFrom().getLongitude(), schedule.getFrom().getLatitude()), schedule.getDirection());
			double curLong = coor.getA();
			double curLat = coor.getB();
			generateTweet(curLong, curLat);
			curCalendar.add(Calendar.MINUTE, 5);
			curMinute = curCalendar.get(Calendar.MINUTE) + 60 * (curCalendar.get(Calendar.HOUR_OF_DAY) - schedule.getDepartureHour());
		}
	}
	
	private void generateTweet(double curLong, double curLat) {
		// add random longitude deviation, which is in 500 meters radium
		double noiseLong = new Random().nextDouble() * 0.0045;
		tweets.add(new Tweet("I have seen the time machine!", curLong + noiseLong, curLat, (Calendar)curCalendar.clone()));
	}
}
