package delay.estimation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class MyLearningEngineTest {
	private MyLearningEngine engine = new MyLearningEngine();
	private Class<?> cls;
	Field cityDelays;
	
	@Before
	public void setup() throws Exception {
		cls = Class.forName("delay.estimation.MyLearningEngine");
		
		Field delays = cls.getDeclaredField("delayData");
		delays.setAccessible(true);
		Map<Location, List<Tuple<Double>>> delayData = new HashMap<Location, List<Tuple<Double>>>();
		delayData.put(Utils.Timbuktu, Arrays.asList(new Tuple<Double>(32.0, 23.0), new Tuple<Double>(44.0, 33.1), new Tuple<Double>(28.0, 21.1)));
		delayData.put(Utils.Tonka, Arrays.asList(new Tuple<Double>(12.0, 9.0), new Tuple<Double>(20.0, 15.1), new Tuple<Double>(16.0, 12.1)));
		delays.set(engine, delayData);
		
		cityDelays = cls.getDeclaredField("cityDelays");
		cityDelays.setAccessible(true);
	}
	
	@Test
	public void testLearnDepartureDelay() throws Exception {
		
		
		Method learnDepartureDelay = cls.getDeclaredMethod("learnDepartureDelay");
		learnDepartureDelay.setAccessible(true);
		learnDepartureDelay.invoke(engine);
			
		
		Map<Location, Tuple<Double>> result = (Map<Location, Tuple<Double>>)cityDelays.get(engine);

		// avg
		Assert.assertEquals((32.0+44+28)/3, result.get(Utils.Timbuktu).getA());
		Assert.assertEquals((12.0+20+16)/3, result.get(Utils.Tonka).getA());
	}
	
	@Test
	public void testLearnArrivalDelay() throws Exception {
		
		Method learnDepartureDelay = cls.getDeclaredMethod("learnArrivalDelay");
		learnDepartureDelay.setAccessible(true);
		learnDepartureDelay.invoke(engine);
			
		Map<Location, Tuple<Double>> result = (Map<Location, Tuple<Double>>)cityDelays.get(engine);
		
		// avg
		Assert.assertEquals(Math.round((12.0+20+16)/3 * 0.75), Math.round(result.get(Utils.Timbuktu).getB()));
		Assert.assertEquals(Math.round((32.0+44+28)/3 * 0.75), Math.round(result.get(Utils.Tonka).getB()));
	}

}
