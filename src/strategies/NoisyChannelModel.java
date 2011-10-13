package strategies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import wordtools.WordUtils;

public class NoisyChannelModel implements CorrectionStrategy {

	enum EDIT {DELETE, INSERT, SUBSTITUTION, REVERSAL};

	float[][] deletionMatrix = new float[26][27];
	float[][] insertionMatrix = new float[26][27];
	float[][] substitutionMatrix = new float[26][26];
	float[][] reversalMatrix = new float[26][26];
	float[] singleCharMatrix = new float[26];
	float[][] multipleCharMatrix = new float[26][26];

	public NoisyChannelModel()
	{
		this.deletionMatrix = getMatrixFromFile(26, 27, "textfiles/deletion_confusion.txt");
		this.insertionMatrix = getMatrixFromFile(26, 27, "textfiles/insertion_confusion.txt");
		this.substitutionMatrix = getMatrixFromFile(26, 26, "textfiles/substitution_confusion.txt");
		this.reversalMatrix = getMatrixFromFile(26, 26, "textfiles/reversal_confusion.txt");
		this.multipleCharMatrix = getMatrixFromFile(26, 26, "textfiles/multi_char_confusion.txt");
		this.singleCharMatrix = getMatrixFromFile(26, "textfiles/single_char_confusion.txt");
	}

	@Override
	public String findCorrection(String inputWord,
			HashMap<String, Integer> dictionaryWords) {

		HashMap<String, Float> candidates = new HashMap<String, Float>();
		for (String key : dictionaryWords.keySet())
		{
			int distance = WordUtils.calculateLevenshteinDistance(inputWord, key);
			if (distance <= 1)
			{
				candidates.put(key, (float) 0);
			}
		}

		for (String candidate : candidates.keySet())
		{
			float freq = dictionaryWords.get(candidate);
			float prior = freq / 44000000;
			int charLocation;
			int insertCorrection;
			int insertTypo;
			float insertionValue;
			float count;

			HashMap<EDIT, Integer> change = findChange(inputWord, candidate);
			
			switch(change.keySet().iterator().next())
			{
			case INSERT:
				charLocation = change.get(EDIT.INSERT);
				insertCorrection = convertCharToInt(candidate.charAt(charLocation - 1));
				insertTypo = convertCharToInt(inputWord.charAt(charLocation));
				insertionValue = this.insertionMatrix[insertCorrection][insertTypo];
				count = this.singleCharMatrix[insertCorrection] * 2000;
				
				if (count == 0)
				{
					candidates.put(candidate, (float) 0);
					break;
				}
				
				candidates.put(candidate, prior * (insertionValue/count));
				break;
			case DELETE:
				charLocation = change.get(EDIT.DELETE);
				
				if (charLocation == 0)
				{
					charLocation = 1;
				}
				
				System.out.println(candidate);
				
				insertCorrection = convertCharToInt(candidate.charAt(charLocation - 1));
				insertTypo = convertCharToInt(candidate.charAt(charLocation));
				insertionValue = this.deletionMatrix[insertCorrection][insertTypo];
				count = this.multipleCharMatrix[insertCorrection][insertTypo] * 2000;

				if (count == 0)
				{
					candidates.put(candidate, (float) 0);
					break;
				}
				
				candidates.put(candidate, prior * (insertionValue/count));
				break;
			case SUBSTITUTION:
				charLocation = change.get(EDIT.SUBSTITUTION);
				
				insertCorrection = convertCharToInt(candidate.charAt(charLocation));
				insertTypo = convertCharToInt(inputWord.charAt(charLocation));
				
				if (insertCorrection > 25 || insertTypo > 25)
				{
					break;
				}
				
				insertionValue = this.substitutionMatrix[insertCorrection][insertTypo];
				count = this.singleCharMatrix[insertCorrection] * 2000;
				
				if (count == 0)
				{
					candidates.put(candidate, (float) 0);
					break;
				}
				
				candidates.put(candidate, prior * (insertionValue/count));
				break;
			case REVERSAL:
				charLocation = change.get(EDIT.REVERSAL);
				
				insertCorrection = convertCharToInt(candidate.charAt(charLocation));
				insertTypo = convertCharToInt(candidate.charAt(charLocation + 1));
				insertionValue = this.reversalMatrix[insertCorrection][insertTypo];
				count = this.multipleCharMatrix[insertCorrection][insertTypo] * 2000;
				
				if (count == 0)
				{
					candidates.put(candidate, (float) 0);
					break;
				}
				
				candidates.put(candidate, prior * (insertionValue/count));
				break;
			}

		}
		
		String max = findMaximumOfHashMap(candidates);
		for (String key : candidates.keySet())
		{
			System.out.printf("Word %s has probability %.20f\n", key, candidates.get(key));
		}
		
		return max;
	}

	public HashMap<EDIT, Integer> findChange(String error, String correction)
	{
		HashMap<EDIT, Integer> editLocation = new HashMap<NoisyChannelModel.EDIT, Integer>();
		if (error.length() == correction.length())
		{
			// Substitution or Transposition
			for (int i = 0; i < error.length(); i++)
			{
				if (error.charAt(i) != correction.charAt(i))
				{	
					if (i == error.length() - 1)
					{
						editLocation.put(EDIT.SUBSTITUTION, i);
						return editLocation;
					}
					
					if (error.charAt(i) == correction.charAt(i + 1) && error.charAt(i + 1) == correction.charAt(i))
					{
						editLocation.put(EDIT.REVERSAL, i);
						return editLocation;
					}

					if (error.charAt(i + 1) == correction.charAt(i + 1))
					{
						editLocation.put(EDIT.SUBSTITUTION, i);
						return editLocation;
					}
				}
			}
		}
		else if (error.length() > correction.length())
		{
			// Must be an insertion
			for (int i = 0; i < correction.length(); i++)
			{
				if (error.charAt(i) != correction.charAt(i))
				{
					if (i == correction.length() - 1)
					{
						editLocation.put(EDIT.INSERT, i);
						return editLocation;
					}
					
					if (error.charAt(i) == correction.charAt(i + 1))
					{
						editLocation.put(EDIT.INSERT, i);
						return editLocation;
					}
				}

			}
			editLocation.put(EDIT.INSERT, error.length() - 1);
			return editLocation;
		}
		else
		{
			// Must be a deletion
			for (int i = 0; i < error.length(); i++)
			{
				if (error.charAt(i) != correction.charAt(i))
				{
					if (correction.charAt(i + 1) == error.charAt(i))
					{
						editLocation.put(EDIT.DELETE, i);
						return editLocation;
					}
				}
			}
			
			editLocation.put(EDIT.DELETE, error.length() - 1);
			return editLocation;
		}

		return editLocation;
	}

	private float[][] getMatrixFromFile(int horizontal, int vertical, String filepath)
	{
		float[][] newReturnMatrix = new float[horizontal][vertical];

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line;
			int y = 0;
			while ((line = reader.readLine()) != null)
			{
				int x = 0;
				String[] numbersFromLine = line.split(" ");
				for (String number : numbersFromLine)
				{
					newReturnMatrix[x][y] = Integer.parseInt(number);
					x++;
				}
				y++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newReturnMatrix;
	}

	private float[] getMatrixFromFile(int horizontal, String filepath)
	{
		float[] newReturnMatrix = new float[horizontal];

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = reader.readLine()) != null)
			{
				int x = 0;
				String[] numbersFromLine = line.split(" ");
				for (String number : numbersFromLine)
				{
					newReturnMatrix[x] = Integer.parseInt(number);
					x++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newReturnMatrix;
	}
	
	int convertCharToInt(char c)
	{
		return (int) c % 97;
	}
	
	String findMaximumOfHashMap(HashMap<String, Float> hashMap)
	{
		float max = 0;
		String currentMax = "";
		
		for (String key : hashMap.keySet())
		{
			float value = hashMap.get(key);
			if (value > max)
			{
				max = value;
				currentMax = key;
			}
		}
		
		return currentMax;
	}

}
