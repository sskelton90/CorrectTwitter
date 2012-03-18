package wordtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public Dictionary()
	{

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

		System.out.println("Learned " + dictionaryWords.size());
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
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(pathToDictionary));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.isEmpty())
				{
					continue;
				}
				
				String[] wordAndValue = line.split("\t");
				if (wordAndValue.length == 2)
				{
					dictionaryWords.put(wordAndValue[0], Integer.parseInt(wordAndValue[1]));
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		System.out.println("Dictionary size is " + dictionaryWords.size());
	}

	/**
	 * Save the current contents of the dictionary to a file with 
	 * given path.
	 * 
	 * @param pathToSave The path to save the file.
	 */
	public void saveDictionary(String pathToSave)
	{
		try 
		{
			FileWriter fstream = new FileWriter("textfiles/dictionary.data");
			BufferedWriter out = new BufferedWriter(fstream);

			for (String key : dictionaryWords.keySet())
			{
				Integer value = dictionaryWords.get(key);
				System.out.printf("Word: %s, Count: %d\n", key, value);
				out.write(key + "\t" + value + "\n");
			}
			
			out.close();

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}


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

	public void setCorrectionStrategy(CorrectionStrategy strategy)
	{
		this.correctionStrategy = strategy;
	}

	int getCountOfWord(String word)
	{
		return (this.dictionaryWords.get(word) == null) ? 0 : this.dictionaryWords.get(word);
	}
	
	public int getDictionarySize()
	{
		return this.dictionaryWords.size();
	}
	
}
