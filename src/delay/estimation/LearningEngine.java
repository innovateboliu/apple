package delay.estimation;

import java.util.List;

import delay.estimation.Utils.ExecutionOrderException;

public interface LearningEngine {
	void parseTweets(List<Tweet> tweets);
	double delayDeparture(Location station) throws ExecutionOrderException ;
	double delayArrival(Location station) throws ExecutionOrderException ;
}
