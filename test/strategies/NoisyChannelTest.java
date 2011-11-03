package strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NoisyChannelTest {

	@Test
	public void testFindChange() {
		NoisyChannelWithLevenshteinModel model = new NoisyChannelWithLevenshteinModel();
		
		assertTrue(model.findChange("acress", "caress").keySet().contains(NoisyChannelWithLevenshteinModel.EDIT.REVERSAL));
		assertEquals(0, (int) model.findChange("acress", "caress").get(NoisyChannelWithLevenshteinModel.EDIT.REVERSAL));
		
		assertTrue(model.findChange("acress", "across").keySet().contains(NoisyChannelWithLevenshteinModel.EDIT.SUBSTITUTION));
		assertEquals(3, (int) model.findChange("acress", "across").get(NoisyChannelWithLevenshteinModel.EDIT.SUBSTITUTION));
		
		assertTrue(model.findChange("acress", "acres").keySet().contains(NoisyChannelWithLevenshteinModel.EDIT.INSERT));
		assertEquals(5, (int) model.findChange("acress", "acres").get(NoisyChannelWithLevenshteinModel.EDIT.INSERT));
		
		assertTrue(model.findChange("acress", "actress").keySet().contains(NoisyChannelWithLevenshteinModel.EDIT.DELETE));
		assertEquals(2, (int) model.findChange("acress", "actress").get(NoisyChannelWithLevenshteinModel.EDIT.DELETE));
	}

	@Test
	public void testCharToInt()
	{
		NoisyChannelWithLevenshteinModel model = new NoisyChannelWithLevenshteinModel();
		
		assertEquals(0, model.convertCharToInt('a'));
		assertEquals(13, model.convertCharToInt('n'));
		assertEquals(3, model.convertCharToInt('d'));
		assertEquals(25, model.convertCharToInt('z'));
	}
}
