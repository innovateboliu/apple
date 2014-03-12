package test;

import java.util.List;

import org.junit.Test;

import delay.LearningEngine;
import delay.Tweet;
import delay.TweetGenerator;
import delay.Utils;

public class LearningEngineTest {
	private TweetGenerator tweetGenerator = new TweetGenerator();
	private LearningEngine engine = new LearningEngine();
	@Test
	public void testCleanData() {
		List<Tweet> tweets = buildTweets();
		engine.parseTweets(tweets);
		double a = engine.delayLeaving(Utils.Timbuktu);
		double b = engine.delayArriving(Utils.Tonka);

		a = engine.delayLeaving(Utils.Tonka);
		b = engine.delayArriving(Utils.Timbuktu);


	}
	
	private List<Tweet> buildTweets() {
		
		// Timbuktu(12:28) to Tonka(14:21)
		// generate Tweets at Timbuktu from 11:50 to 11:50 24hr
		return tweetGenerator.generateTweets(1394437800000L, 1209600000);
	}

}
