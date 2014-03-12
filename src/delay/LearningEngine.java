package delay;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class LearningEngine {

	private Map<Location, List<Tuple>> delayData;
	private Map<Location, Tuple> cityDelays;
	private SimpleRegression regressionEngine;
	private boolean isLearningDepartureDelayDone;

	public LearningEngine() {
		this.delayData = new HashMap<Location, List<Tuple>>();
		cityDelays = new HashMap<Location, Tuple>();
		regressionEngine = new SimpleRegression();
		isLearningDepartureDelayDone = false;
	}

	public void parseTweets(List<Tweet> tweets) {
		cleanData(tweets);
		learnDepartureDelay();
		learnArrivalDelay();
	}

	private void cleanData(List<Tweet> tweets) {
		Schedule[] schedules = Utils.buildSchedules();
		int curHour = 0;
		int curMinutes = 0;
		boolean isRunning = false;
		Tuple delayTuple = new Tuple();
		for (Tweet tweet : tweets) {
			curHour = tweet.getCalendar().get(Calendar.HOUR_OF_DAY);
			curMinutes = tweet.getCalendar().get(Calendar.MINUTE);
			if (!isRunning) { // the train is still at station
				if (curHour % 6 > 0 && curHour % 6 <= 1) {
					double curLong = tweet.getLongitude();
					double curLat = tweet.getLatitude();
					Schedule schedule = schedules[curHour / 6];
					double fromLong = schedule.getFrom().getLongitude();
					double fromLat = schedule.getFrom().getLatitude();
					
					//first tweet after the train departures
					double distance = Utils.calcDistance(curLong, curLat, fromLong, fromLat);
					if (distance > 500) {
						double speed = Utils.DISTANCE / Utils.TIME;
						int runningMinutes = (int)(distance / speed);
						int delayMinutes = curMinutes + 60 * (curHour - schedule.getDepartureHour()) - runningMinutes;
						delayTuple.setA(delayMinutes);
						isRunning = true;
					}
				}
			} else { // the train is on the way
				
			}

		}
	}

	private void learnDepartureDelay() {
		for (Map.Entry<Location, List<Tuple>> cityDelay : delayData.entrySet()) {
			Location station = cityDelay.getKey();
			List<Tuple> delays = cityDelay.getValue();
			int num = delays.size();
			double sumDepartureDelay = 0;
			for (int i = 0; i < num; i++) {
				sumDepartureDelay += delays.get(i).getA();
			}
			Tuple estimatedDelay = new Tuple();
			estimatedDelay.setA(sumDepartureDelay / num);
			cityDelays.put(station, estimatedDelay);
		}
		isLearningDepartureDelayDone = true;
	}

	private void learnArrivalDelay() {
		if (!isLearningDepartureDelayDone) {
			learnDepartureDelay();
		}
		for (Map.Entry<Location, List<Tuple>> cityDelay : delayData.entrySet()) {
			regressionEngine.clear();
			Location station = cityDelay.getKey();
			List<Tuple> delays = cityDelay.getValue();
			int size = delays.size();
			double[][] input = new double[size][2];
			for (int i = 0; i < size; i++) {
				input[i][0] = delays.get(i).getA();
				input[i][1] = delays.get(i).getB();
			}
			regressionEngine.addData(input);
			double estimatedDepartureDelay = cityDelays.get(station).getA();
			double estimatedArrivalDelay = regressionEngine.getIntercept()
					+ regressionEngine.getSlope() * estimatedDepartureDelay;
			cityDelays.get(station).setB(estimatedArrivalDelay);
		}
	}

	public double delayLeaving(Location station) {
		return cityDelays.get(station).getA();
	}

	public double delayArriving(Location station) {
		return cityDelays.get(station).getB();
	}

	public static void main(String[] args) {
		LearningEngine engine = new LearningEngine();
		double[][] data = { { 100, 72 }, { 60, 44 }, { 20, 17 }, { 40, 32 },
				{ 36, 24 }, { 16, 10 } };
		engine.regressionEngine.addData(data);
		System.out.println(engine.regressionEngine.getSlope());
		System.out.println(engine.regressionEngine.getIntercept());
		System.out.println(engine.regressionEngine.getSlopeStdErr());
	}
}
