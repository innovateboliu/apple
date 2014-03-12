package delay_estimation;

import java.util.List;

import org.junit.Test;

import delay_estimation.MyLearningEngine;
import delay_estimation.Tweet;
import delay_estimation.TweetGenerator;
import delay_estimation.Utils;
import delay_estimation.Utils.ExecutionOrderException;

public class LearningEngineTest {
	private TweetGenerator tweetGenerator = new TweetGenerator();
	private MyLearningEngine engine = new MyLearningEngine();
	@Test
	public void testCleanData() throws ExecutionOrderException {
		List<Tweet> tweets = buildTweets();
		engine.parseTweets(tweets);
		System.out.println(engine.delayDeparture(Utils.Timbuktu));
		System.out.println(engine.delayArrival(Utils.Tonka));
		
		System.out.println(engine.delayDeparture(Utils.Tonka));
		System.out.println(engine.delayArrival(Utils.Timbuktu));
	}
	
	private List<Tweet> buildTweets() {
		
		// Timbuktu(12:28) to Tonka(14:21)
		// generate Tweets at Timbuktu from 11:50 to 11:50 24hr
		return tweetGenerator.generateTweets(1394437800000L, 1209600000);
	}

}
