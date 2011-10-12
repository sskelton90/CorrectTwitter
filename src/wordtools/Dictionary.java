package wordtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import strategies.CorrectionStrategy;

public class Dictionary {
	
	/**
	 * The current contents of the dictionary
	 */
	private HashMap<String, Integer> dictionaryWords = new HashMap<String, Integer>();
	private CorrectionStrategy correctionStrategy;
	
	/**
	 * Instantiate a new Dictionary.
	 */
	public Dictionary(CorrectionStrategy strategy)
	{
		this.correctionStrategy = strategy;
	}
	
	/**
	 * Instantiate a new Dictionary and automatically load the
	 * saved dictionary at the given file path.
	 * 
	 * @param initialDictionaryPath The path to the dictionary file
	 */
	public Dictionary(String initialDictionaryPath, CorrectionStrategy strategy)
	{
		loadDictionary(initialDictionaryPath);
		this.correctionStrategy = strategy;
	}
	
	/**
	 * 
	 */
	public void learnFromListOfWords(List<String> words)
	{
		for (String word : words)
		{
			if (!this.dictionaryWords.containsKey(word))
			{
				this.dictionaryWords.put(word, 1);
			}
			else
			{
				this.dictionaryWords.put(word, this.dictionaryWords.get(word) + 1);
			}
		}
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
		return this.dictionaryWords.get(wordToCheck) != null;
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
	 * Corrects the word based on the current {@link CorrectionStrategy}.
	 * 
	 * @param inputWord The word that all dictionary entries will be compared
	 * 					to.
	 * 
	 * @return The nearest word in the dictionary.
	 */
	public String correctWord(String inputWord)
	{
		return this.correctionStrategy.findCorrection(inputWord, this.dictionaryWords);
	}

	
	int getCountOfWord(String word)
	{
		return (this.dictionaryWords.get(word) == null) ? 0 : this.dictionaryWords.get(word);
	}
	
	public void dumpDictionaryData()
	{
		HashMap<String, Integer> sortedDictionary = this.sortStringIntegerHashMap(this.dictionaryWords, Collections.reverseOrder());
		for (String key : sortedDictionary.keySet())
		{
			System.out.printf("Word: %s, Count: %d\n", key, sortedDictionary.get(key));
		}
	}
	
	private HashMap<String, Integer> sortStringIntegerHashMap(HashMap<String, Integer> inputMap, Comparator comparator)
	{
	    Map<String, Integer> tempMap = new HashMap<String, Integer>();
	    for (String wsState : inputMap.keySet()){
	        tempMap.put(wsState, inputMap.get(wsState));
	    }

	    List<String> mapKeys = new ArrayList<String>(tempMap.keySet());
	    List<Integer> mapValues = new ArrayList<Integer>(tempMap.values());
	    HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	    TreeSet<Integer> sortedSet = new TreeSet<Integer>(comparator);
	    sortedSet.addAll(mapValues);
	    Object[] sortedArray = sortedSet.toArray();
	    int size = sortedArray.length;
	    for (int i=0; i<size; i++){
	        sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), 
	                      (Integer)sortedArray[i]);
	    }
	    
	     return sortedMap;
	}

}
