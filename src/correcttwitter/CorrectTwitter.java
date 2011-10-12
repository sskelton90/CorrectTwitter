package correcttwitter;

import java.util.ArrayList;
import java.util.HashMap;

import strategies.MostCommonWord;
import wordtools.Dictionary;
import wordtools.WordUtils;

public class CorrectTwitter {
	
	public static void main(String[] args)
	{
		ArrayList<String> words = WordUtils.getWordsFromFile("textfiles/big.txt");
		
		Dictionary dict = new Dictionary(new MostCommonWord());
		dict.learnFromListOfWords(words);
		
		dict.dumpDictionaryData();
		
		HashMap<String, String> errorsCorrections = WordUtils.getErrorsAndCorrectionsFromFile("textfiles/SHEFFIELDDAT.643");
		
		float correctCount = 0;
		float incorrectCount = 0;
		
		for (String correctWord : errorsCorrections.keySet())
		{
			String error = errorsCorrections.get(correctWord);
			if (!dict.wordIsInDictionary(error))
			{
				System.out.printf("Word not found: %s\n", error);
				
				String nearestWord = dict.correctWord(error);
				
				if (!nearestWord.equals(correctWord))
				{
					incorrectCount++;
					System.out.printf("System corrected %s to %s but real correction was %s\n", error, nearestWord, correctWord);
				}
				else
				{
					correctCount++;
					System.out.println("Correct! Correction was: " + nearestWord);
				}
			}
		}
		
		System.out.println("Accuracy rate: " + (correctCount / (correctCount + incorrectCount)));
		
	}

}
