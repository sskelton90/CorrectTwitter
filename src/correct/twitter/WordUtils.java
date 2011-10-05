package correct.twitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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
				String[] wordsFromLine = line.split(" ");
				for (String word : wordsFromLine)
				{
					String normalisedString = word.replaceAll("[^a-zA-Z\'-]", "").toLowerCase();
					if (normalisedString.isEmpty())
					{
						continue;
					}
					words.add(normalisedString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return words;
	}
	
}
