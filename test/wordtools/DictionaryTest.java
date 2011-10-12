package wordtools;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Test;

import strategies.CorrectionStrategy;
import wordtools.Dictionary;


public class DictionaryTest {

	@Test
	public void testLearnFromListOfWords()
	{
		ArrayList<String> wordsToLearn = new ArrayList<String>();
		wordsToLearn.add("hello");
		wordsToLearn.add("hello");
		wordsToLearn.add("steven");
		
		Dictionary dict = new Dictionary(EasyMock.createNiceMock(CorrectionStrategy.class));
		dict.learnFromListOfWords(wordsToLearn);
		
		assertEquals(true, dict.wordIsInDictionary("hello"));
		assertEquals(2, dict.getCountOfWord("hello"));
		assertEquals(1, dict.getCountOfWord("steven"));
	}
}
