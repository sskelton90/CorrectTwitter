package strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NoisyChannelTest {

	@Test
	public void testFindChange() {
		NoisyChannelModel model = new NoisyChannelModel();
		
		assertTrue(model.findChange("acress", "caress").keySet().contains(NoisyChannelModel.EDIT.REVERSAL));
		assertEquals(0, (int) model.findChange("acress", "caress").get(NoisyChannelModel.EDIT.REVERSAL));
		
		assertTrue(model.findChange("acress", "across").keySet().contains(NoisyChannelModel.EDIT.SUBSTITUTION));
		assertEquals(3, (int) model.findChange("acress", "across").get(NoisyChannelModel.EDIT.SUBSTITUTION));
		
		assertTrue(model.findChange("acress", "acres").keySet().contains(NoisyChannelModel.EDIT.INSERT));
		assertEquals(5, (int) model.findChange("acress", "acres").get(NoisyChannelModel.EDIT.INSERT));
		
		assertTrue(model.findChange("acress", "actress").keySet().contains(NoisyChannelModel.EDIT.DELETE));
		assertEquals(2, (int) model.findChange("acress", "actress").get(NoisyChannelModel.EDIT.DELETE));
	}

	@Test
	public void testCharToInt()
	{
		NoisyChannelModel model = new NoisyChannelModel();
		
		assertEquals(0, model.convertCharToInt('a'));
		assertEquals(13, model.convertCharToInt('n'));
		assertEquals(3, model.convertCharToInt('d'));
		assertEquals(25, model.convertCharToInt('z'));
	}
}
