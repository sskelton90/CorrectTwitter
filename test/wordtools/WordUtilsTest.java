package wordtools;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import wordtools.WordUtils;


public class WordUtilsTest {

	@Test
	public void testGetWordsFromFile() {
		ArrayList<String> words = WordUtils.getWordsFromFile("textfiles/test-file");

		// Testing possible edge case where words contain
		// apostrophes.
		assertEquals(true, words.contains("don't"));
		
		// Testing another case of inner punctuation
		assertEquals(true, words.contains("test-file"));
		
		assertEquals(27, words.size());
	}
	
	@Test
	public void testMinimumOfArray() 
	{
		int[] array = new int[] {10, 4, 5, 9};
		
		assertEquals(4, WordUtils.minimumOfArray(array));
	}
	
	@Test
	public void testLevenshteinDistance()
	{
		assertEquals(3, WordUtils.calculateLevenshteinDistance("sitting", "kitten"));
		assertEquals(3, WordUtils.calculateLevenshteinDistance("saturday", "sunday"));
		assertEquals(6, WordUtils.calculateLevenshteinDistance("YHCQPGK", "LAHYQQKPGKA"));
		assertEquals(1, WordUtils.calculateLevenshteinDistance("acress", "actress"));
		assertEquals(2, WordUtils.calculateLevenshteinDistance("important", "importnat"));
	}

}
