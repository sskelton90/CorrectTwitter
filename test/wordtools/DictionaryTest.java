package wordtools;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;


public class DictionaryTest {

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
