package correct.twitter;

import java.util.Arrays;
import java.util.HashMap;

public class Dictionary {
	
	/**
	 * The current contents of the dictionary
	 */
	private HashMap<String, Integer> dictionaryWords;
	
	/**
	 * Instantiate a new Dictionary.
	 */
	public Dictionary()
	{
		
	}
	
	/**
	 * Instantiate a new Dictionary and automatically load the
	 * saved dictionary at the given file path.
	 * 
	 * @param initialDictionaryPath The path to the dictionary file
	 */
	public Dictionary(String initialDictionaryPath)
	{
		
	}
	
	/**
	 * Add the word to the dictionary if it's not already present.
	 * 
	 * @param wordToAdd The word to add to the dictionary.
	 */
	public void addWord(String wordToAdd)
	{
		
	}
	
	/**
	 * Delete a given word from the dictionary if present.
	 * 
	 * @param wordToDelete The word to delete from the dictionary.
	 */
	public void deleteWord(String wordToDelete)
	{
		
	}
	
	/**
	 * A simple lookup to check if the given word is in the 
	 * dictionary.
	 * 
	 * @param wordToCheck The word to look in the dictionary for.
	 * 
	 * @return True if the word is in the dictionary, false if not.
	 */
	public boolean wordIsInDictionary(String wordToCheck)
	{
		return false;
	}
	
	/**
	 * Load a dictionary file from a given path.
	 * 
	 * @param pathToDictionary The path of the dictionary file.
	 */
	public void loadDictionary(String pathToDictionary)
	{
		
	}
	
	/**
	 * Save the current contents of the dictionary to a file with 
	 * given path.
	 * 
	 * @param pathToSave The path to save the file.
	 */
	public void saveDictionary(String pathToSave)
	{
		
	}
	
	/**
	 * Finds the nearest word, in the dictionary, to the input word
	 * supplied.
	 * 
	 * @param inputWord The word that all dictionary entries will be compared
	 * 					to.
	 * 
	 * @return The nearest word in the dictionary.
	 */
	public String findNearest(String inputWord)
	{
		return "";
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
	int calculateLevenshteinDistance(String firstWord, String secondWord)
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
	
	/**
	 * Calculates the minimum of an integer array.
	 * 
	 * @param array The array to find the minimum of.
	 * 
	 * @return The minimum of the given array.
	 */
	int minimumOfArray(int[] array)
	{
		Arrays.sort(array);
		return array[0];
	}

}
