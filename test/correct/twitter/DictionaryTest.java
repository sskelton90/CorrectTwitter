package correct.twitter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

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
	
	@Test
	public void testLearnFromListOfWords()
	{
		ArrayList<String> wordsToLearn = new ArrayList<String>();
		wordsToLearn.add("hello");
		wordsToLearn.add("hello");
		wordsToLearn.add("steven");
		
		Dictionary dict = new Dictionary();
		dict.learnFromListOfWords(wordsToLearn);
		
		assertEquals(true, dict.wordIsInDictionary("hello"));
		assertEquals(2, dict.getCountOfWord("hello"));
		assertEquals(1, dict.getCountOfWord("steven"));
	}
}
