1. Source code file structure
	- src/delay/estimation/ contains all source code including a SampleDriver which provides example of how to run the code.

	- test/delay/estimation contains all unit test files, currently it only contains several simple test cases for the learning engine.


2. Assumption of solutions
	- Delay of departure. Due to there is no external factors which affects the delay of departure, it's rational to believe the delay conform to Gaussian Distribution. Therefore the mean of departure delay at Timbuktu is 30 minutes and the variance is 10 minutes, for Tonka the mean is 15 and the variance is 5 minutes.

	- Delay of arrival. Also due to there is no external factors which affect the delay of arrival, and the experience that once the departure delay happens, the driver will always speed up the train to reduce the arrival delay. So it's rational to believe there is a linear relation between the arrival delay and departure delay as arrivalDelay = b + a * departureDelay. Here we assume the b is 0 and a is 0.75. 


3. Solution for TweetGenerator
	- Tweets are generated every five minutes, either when the train is at station or on the way.
	- When the train is at station, the tweets are generated with the current time, any long/lat location within the circumference of 500 meters from the station.
	- When the train is on the way, it's speed is calculated by distance / (120 minuts - departureDelay + arrivalDelay), the location of each tweet - within the circumference of 500 meters from the train location - is calculated by current time, train speed, departure time and departure location.
	- To simplify the problem, long/lat of Timbuktu is (0, 0), Tonka is (1.08, 0) which means their distance is 120km.
	- The departure delay is generated randomly however conforms to Gaussian Distribution as mentioned above. And the arrival delay is generated according to the linear relation with departure delay, which means implicitly the speed of train will be higher than normal.


4. Solution for LearningEngine
	- First of all the engine retrives and calculates all departure/arrival delay of both stations.
	- The estimated departure delay is obtained through calculating the mean of all departure delay. As to the fact that departure delay conforms to Gaussian Distribution, the sample which maximums the probability density function is the mean of all samples.
	- The estimated arrival delay is obtained by executing Least Square regression on the already calculated estimated departure delay and the retrieved arrival delays. 


