package delay_estimation;

import java.util.List;

import delay_estimation.Utils.ExecutionOrderException;

public class SampleDriver {
	private TweetGenerator tweetGenerator = new TweetGenerator();
	private MyLearningEngine engine = new MyLearningEngine();

	public static void main(String[] args) throws ExecutionOrderException{
		SampleDriver driver = new SampleDriver();
		
		// from 03/10/2014 00:50 to 03/24/2014 00:50 PST
		List<Tweet> tweets = driver.tweetGenerator.generateTweets(1394437800000L, 1209600000);
		driver.engine.parseTweets(tweets);
		
		System.out.println("Estimated departure delay(in minutes) at Timbuktu is " + driver.engine.delayDeparture(Utils.Timbuktu));
		System.out.println("Estimated arrival delay(in minutes) at Tonka is " + driver.engine.delayArrival(Utils.Tonka));
		
		System.out.println("Estimated departure delay(in minutes) at Tonka is " + driver.engine.delayDeparture(Utils.Tonka));
		System.out.println("Estimated arrival delay(in minutes) at Timbuktu is " + driver.engine.delayArrival(Utils.Timbuktu));
	}
	

}
