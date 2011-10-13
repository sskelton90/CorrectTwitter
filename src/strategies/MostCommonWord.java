package strategies;

import java.util.ArrayList;
import java.util.HashMap;

import wordtools.WordUtils;


public class MostCommonWord implements CorrectionStrategy {

	@Override
	public String findCorrection(String inputWord, HashMap<String, Integer> dictionaryWords) {
		ArrayList<String> candidates = new ArrayList<String>();
		for (String key : dictionaryWords.keySet())
		{
			int distance = WordUtils.calculateLevenshteinDistance(inputWord, key);
			if (distance <= 1)
			{
				candidates.add(key);
			}
		}
		
		int maxValue = 0;
		String maxWord = "";
		
		for (String candidate : candidates)
		{
			int prior = dictionaryWords.get(candidate);
			if (prior > maxValue)
			{
				maxValue = prior;
				maxWord = candidate;
			}
		}
		
		return maxWord;
	}

}
