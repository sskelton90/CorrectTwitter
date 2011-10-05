package correct.twitter;

import java.util.ArrayList;

public class CorrectTwitter {
	
	public static void main(String[] args)
	{
		ArrayList<String> words = WordUtils.getWordsFromFile("textfiles/big.txt");
		
		Dictionary dict = new Dictionary();
		dict.learnFromListOfWords(words);
		
		dict.dumpDictionaryData();
		
		ArrayList<String> errorWords = WordUtils.getWordsFromFile("textfiles/test-errors");
		
		for (String word : errorWords)
		{
			if (!dict.wordIsInDictionary(word))
			{
				System.out.printf("Word not found: %s\n", word);
				System.out.println(dict.findNearest(word));
			}
		}
		
	}

}
