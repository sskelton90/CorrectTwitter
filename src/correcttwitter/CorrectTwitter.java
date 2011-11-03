package correcttwitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import strategies.MostCommonWord;
import strategies.NoisyChannelNorvig;
import strategies.NoisyChannelWithLevenshteinModel;

import wordtools.Dictionary;
import wordtools.WordUtils;

public class CorrectTwitter {
	
	public static void main(String[] args)
	{
		Dictionary dict = new Dictionary();
		
		if (args[0].equals("train"))
		{
			System.out.println("Training!");
			
			if (args.length == 1)
			{
				System.out.println("Please enter some training data.");
				System.exit(0);
			}
			
			for (int i = 1; i < args.length; i++)
			{
				ArrayList<String> words = WordUtils.getWordsFromFile(args[i]);
				dict.learnFromListOfWords(words);
			}
			
			dict.saveDictionary("textfiles/dictionary.data");
		}
		else if (args[0].equals("correct"))
		{
			if (args.length != 4)
			{
				System.out.println("Please provide a dictionary file, correction strategy, and test file.");
				System.exit(0);
			}
			
			if (args[1].equals("mostcommon"))
			{
				dict.setCorrectionStrategy(new MostCommonWord());
				System.out.println("Using the Most Common Word Model.");
			}
			else if (args[1].equals("noisychannel"))
			{
				dict.setCorrectionStrategy(new NoisyChannelWithLevenshteinModel());
				System.out.println("Using the Noisy Channel Model.");
			}
			else if (args[1].equals("noisychannelnorvig"))
			{
				dict.setCorrectionStrategy(new NoisyChannelNorvig());
				System.out.println("Using the Noisy Channel defined by Norvig");
			}
			else
			{
				System.out.println("Please provide a valid correction strategy");
				System.exit(0);
			}
			
			dict.loadDictionary(args[2]);
			HashMap<String, String> errorsCorrections = WordUtils.getErrorsAndCorrectionsFromFile(args[3]);

			float correctCount = 0;
			float incorrectCount = 0;

			for (String correctWord : errorsCorrections.keySet())
			{
				String error = errorsCorrections.get(correctWord);
				if (!dict.wordIsInDictionary(error))
				{
					String nearestWord = dict.correctWord(error);

					if (!nearestWord.equals(correctWord))
					{
						incorrectCount++;
						System.out.printf("System corrected %s to %s but real correction was %s\n", error, nearestWord, correctWord);
					}
					else
					{
						correctCount++;
					}
				}
			}

			System.out.println("Accuracy rate: " + (correctCount / (correctCount + incorrectCount)));
		}
		else if (args[0].equals("tweets"))
		{
			if (args[1].equals("mostcommon"))
			{
				dict.setCorrectionStrategy(new MostCommonWord());
				System.out.println("Using the Most Common Word Model.");
			}
			else if (args[1].equals("noisychannel"))
			{
				dict.setCorrectionStrategy(new NoisyChannelWithLevenshteinModel());
				System.out.println("Using the Noisy Channel Model.");
			}
			else if (args[1].equals("noisychannelnorvig"))
			{
				dict.setCorrectionStrategy(new NoisyChannelNorvig());
				System.out.println("Using the Noisy Channel defined by Norvig");
			}
			else
			{
				System.out.println("Please provide a valid correction strategy");
				System.exit(0);
			}
			
			dict.loadDictionary(args[2]);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(args[3]));
				String line;
				while ((line = reader.readLine()) != null)
				{
					if (line.isEmpty())
					{
						continue;
					}
					
					StringBuilder sb = new StringBuilder();
					String[] words = line.split(" ");
					
					for (String word : words)
					{
						sb.append(dict.correctWord(word.toLowerCase()));
						sb.append(" ");
					}
					sb.append("\n");
					
					System.out.println("Original: " + line);
					System.out.println("Corrected: " + sb.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
