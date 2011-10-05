package correct.twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class WordUtilsTest {

	@Test
	public void testGetWordsFromFile() {
		ArrayList<String> words = WordUtils.getWordsFromFile("textfiles/test-file");
		
		for (String word : words)
		{
			System.out.println(word);
		}
		
		// Testing possible edge case where words contain
		// apostrophes.
		assertEquals(true, words.contains("don't"));
		
		// Testing another case of inner punctuation
		assertEquals(true, words.contains("test-file"));
		
		assertEquals(27, words.size());
	}

}
