package correct.twitter;

import static org.junit.Assert.*;

import org.junit.Test;

public class DictionaryTest {

	@Test
	public void testMinimumOfArray() 
	{
		Dictionary dict = new Dictionary();
		
		int[] array = new int[] {10, 4, 5, 9};
		
		assertEquals(4, dict.minimumOfArray(array));
	}
	
	@Test
	public void testLevenshteinDistance()
	{
		Dictionary dict = new Dictionary();
		
		assertEquals(3, dict.calculateLevenshteinDistance("sitting", "kitten"));
		assertEquals(3, dict.calculateLevenshteinDistance("saturday", "sunday"));
		assertEquals(6, dict.calculateLevenshteinDistance("YHCQPGK", "LAHYQQKPGKA"));
	}
}
