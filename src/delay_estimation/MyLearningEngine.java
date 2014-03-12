package delay_estimation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import delay_estimation.Utils.ExecutionOrderException;

public class MyLearningEngine implements LearningEngine{

	private Map<Location, List<Tuple<Double>>> delayData;	// contains the all departure/arrival delay data of the two city(as start station)  
	private Map<Location, Tuple<Double>> cityDelays;		// the result which contains the departure/arrival delay of the two cities
	private SimpleRegression regressionEngine;				// the engine which use Least Square Regression to generate linear relation between departure delay and arrival delay
	private boolean isLearningDepartureDelayDone;	
	private boolean isLearningDone;

	public MyLearningEngine() {
		this.delayData = new HashMap<Location, List<Tuple<Double>>>();
		cityDelays = new HashMap<Location, Tuple<Double>>();
		regressionEngine = new SimpleRegression();
		isLearningDepartureDelayDone = false;
		isLearningDone = false;
	}

	public void parseTweets(List<Tweet> tweets) {
		cleanData(tweets);
		learnDepartureDelay();
		learnArrivalDelay();
		isLearningDone = true;
	}

	private void cleanData(List<Tweet> tweets) {
		Schedule[] schedules = Utils.buildSchedules();
		int curHour = 0;
		int curMinutes = 0;
		boolean isRunning = false;
		Tuple<Double> delayTuple = new Tuple<Double>();
		Tweet previousTweet = null;
		
		delayData.put(Utils.Timbuktu, new ArrayList<Tuple<Double>>());
		delayData.put(Utils.Tonka, new ArrayList<Tuple<Double>>());

		for (Tweet tweet : tweets) {
			curHour = tweet.getCalendar().get(Calendar.HOUR_OF_DAY);
			curMinutes = tweet.getCalendar().get(Calendar.MINUTE);
			
			double curLong = tweet.getLongitude();
			double curLat = tweet.getLatitude();
			Schedule schedule = schedules[curHour / 6];
			
			if (!isRunning) { // the train is still at station
				if (curHour % 6 >= 0 && curHour % 6 <= 1) {
					double fromLong = schedule.getFrom().getLongitude();
					double fromLat = schedule.getFrom().getLatitude();
					
					//first tweet after the train departures
					double distance = Utils.calcDistance(curLong, curLat, fromLong, fromLat);
					if (distance > 500) {
						double speed = Utils.DISTANCE / Utils.TIME;
						int runningMinutes = (int)(distance / speed);
						int delayMinutes = curMinutes + 60 * (curHour - schedule.getDepartureHour()) - runningMinutes;
						delayTuple.setA((double)delayMinutes);
						isRunning = true;
					}
				}
			} else { // the train is on the way
				double toLong = schedule.getTo().getLongitude();
				double toLat = schedule.getTo().getLatitude();
				double distance = Utils.calcDistance(curLong, curLat, toLong, toLat);
				if (distance < 500) {
					double preLong = previousTweet.getLongitude();
					double preLat = previousTweet.getLatitude();
					distance = Utils.calcDistance(preLong, preLat, toLong, toLat);
					int preMinutes = previousTweet.getCalendar().get(Calendar.MINUTE);
					double speed = Utils.DISTANCE / Utils.TIME;
					int runningMinutes = (int)(distance / speed);
					int delayMinutes = (preMinutes + runningMinutes) % 60 ;
					delayTuple.setB((double)delayMinutes);
					isRunning = false;
					Location from = schedule.getFrom();
					delayData.get(from).add(delayTuple.clone());
				}
				
			}
			previousTweet = tweet;
		}
	}

	private void learnDepartureDelay() {
		for (Map.Entry<Location, List<Tuple<Double>>> cityDelay : delayData.entrySet()) {
			Location from = cityDelay.getKey();
			List<Tuple<Double>> delays = cityDelay.getValue();
			int num = delays.size();
			double sumDepartureDelay = 0;
			for (int i = 0; i < num; i++) {
				sumDepartureDelay += delays.get(i).getA();
			}
			Tuple<Double> estimatedDelay = new Tuple<Double>();
			estimatedDelay.setA(sumDepartureDelay / num);
			cityDelays.put(from, estimatedDelay);
		}
		isLearningDepartureDelayDone = true;
	}

	private void learnArrivalDelay() {
		if (!isLearningDepartureDelayDone) {
			learnDepartureDelay();
		}
		for (Map.Entry<Location, List<Tuple<Double>>> cityDelay : delayData.entrySet()) {
			regressionEngine.clear();
			Location from = cityDelay.getKey();
			Location to = from.getName().equals("Timbuktu") ? Utils.Tonka : Utils.Timbuktu;
			List<Tuple<Double>> delays = cityDelay.getValue();
			int size = delays.size();
			double[][] input = new double[size][2];
			for (int i = 0; i < size; i++) {
				input[i][0] = delays.get(i).getA();
				input[i][1] = delays.get(i).getB();
			}
			regressionEngine.addData(input);
			double estimatedDepartureDelay = cityDelays.get(from).getA();
			double estimatedArrivalDelay = regressionEngine.getIntercept()
					+ regressionEngine.getSlope() * estimatedDepartureDelay;
			cityDelays.get(to).setB(estimatedArrivalDelay);
		}
	}

	public double delayDeparture(Location station) throws ExecutionOrderException {
		if (!isLearningDone) {
			throw new ExecutionOrderException("Please run parseTweets first");
		}
		return cityDelays.get(station).getA();
	}

	public double delayArrival(Location station) throws ExecutionOrderException {
		if (!isLearningDone) {
			throw new ExecutionOrderException("Please run parseTweets first");
		}
		return cityDelays.get(station).getB();
	}

	public static void main(String[] args) {
		MyLearningEngine engine = new MyLearningEngine();
		double[][] data = { { 100, 72 }, { 60, 44 }, { 20, 17 }, { 40, 32 },
				{ 36, 24 }, { 16, 10 } };
		engine.regressionEngine.addData(data);
		System.out.println(engine.regressionEngine.getSlope());
		System.out.println(engine.regressionEngine.getIntercept());
		System.out.println(engine.regressionEngine.getSlopeStdErr());
	}
}
