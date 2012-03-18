package wordtools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class WordUtils {
	
	public static ArrayList<String> getWordsFromFile(String filepath)
	{
		ArrayList<String> words = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("<"))
				{
					continue;
				}
//				String normalisedString = line.replaceAll("[\\p{Punct}&&[^\']]", " ").toLowerCase();
//				System.out.println(normalisedString);
//				String[] wordsFromLine = normalisedString.split(" ");
				
				TokenStream ts = new MyAnalyzer().tokenStream("default", new StringReader(line));
				TermAttribute termAtt = ts.getAttribute(TermAttribute.class); 
				while (ts.incrementToken())
				{
		            words.add(termAtt.term()); 
				}
			}
			
			reader.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return words;
	}
	
	public static HashMap<String, String> getErrorsAndCorrectionsFromFile(String filepath)
	{
		HashMap<String, String> errorsCorrections = new HashMap<String, String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] wordsFromLine = line.split(" +");
				errorsCorrections.put(wordsFromLine[0].toLowerCase(), wordsFromLine[1].toLowerCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return errorsCorrections;
	}
	
	/**
	 * Calculates the Levenshtein Distance (aka the edit distance) 
	 * between firstWord and secondWord.
	 * 
	 * @param firstWord
	 * @param secondWord
	 * 
	 * @return The integer value of the Levenshtein distance between the two words
	 * @see <a href="http://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein Distance</a>
	 */
	public static int calculateLevenshteinDistance(String firstWord, String secondWord)
	{
		int firstLength = firstWord.length();
		int secondLength = secondWord.length();
		
		int[][] distanceMatrix = new int[firstLength + 1][secondLength + 1];
		
		for (int i = 0; i <= firstLength; i++)
		{
			distanceMatrix[i][0] = i;
		}
		
		for (int j = 0; j <= secondLength; j++)
		{
			distanceMatrix[0][j] = j;
		}
		
		for (int i = 1; i <= firstLength; i++)
		{
			for (int j = 1; j <= secondLength; j++)
			{
				int substitutionCost = (firstWord.charAt(i - 1) == secondWord.charAt(j - 1) ? 0 : 1);
				
				// Calculate the minimum edit cost from deletion, insertion
				// substitution respectively.
				int[] editCosts = new int[3];
				editCosts[0] = distanceMatrix[i-1][j] + 1;
				editCosts[1] = distanceMatrix[i][j-1] + 1;
				editCosts[2] = distanceMatrix[i-1][j-1] + substitutionCost;
				
				distanceMatrix[i][j] = minimumOfArray(editCosts);
			}
		}
		
		return distanceMatrix[firstLength][secondLength];
	}
	
	public static ArrayList<String> wordsFromXml(String xmlPath)
	{
		ArrayList<String> words = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(xmlPath));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("<"))
				{
					continue;
				}
				String normalisedString = line.replaceAll("[\\p{Punct}&&[^\']]", " ").toLowerCase();
				System.out.println(normalisedString);
				String[] wordsFromLine = normalisedString.split(" ");
				for (String word : wordsFromLine)
				{
					if (normalisedString.isEmpty() || word.contains("<"))
					{
						continue;
					}
					words.add(normalisedString);
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return words;
	}
	
	/**
	 * Calculates the minimum of an integer array.
	 * 
	 * @param array The array to find the minimum of.
	 * 
	 * @return The minimum of the given array.
	 */
	public static int minimumOfArray(int[] array)
	{
		Arrays.sort(array);
		return array[0];
	}
	
}
