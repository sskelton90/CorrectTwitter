package strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoisyChannelNorvig implements CorrectionStrategy {

	String alphabet = "abcdefghijklmnopqrstuvwxyz";

	@Override
	public String findCorrection(String inputWord,
			HashMap<String, Integer> dictionaryWords) {
		if(dictionaryWords.containsKey(inputWord) || isUrl(inputWord) || isAtUsername(inputWord) || isHashtag(inputWord)) return inputWord;
		ArrayList<String> list = edits(inputWord);
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for(String s : list) if(dictionaryWords.containsKey(s)) candidates.put(dictionaryWords.get(s),s);
		if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
		for(String s : list) for(String w : edits(s)) if(dictionaryWords.containsKey(w)) candidates.put(dictionaryWords.get(w),w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : inputWord;
	}
	
	public ArrayList<String> edits(String word)
	{
		ArrayList<String> edits = new ArrayList<String>();
		
		edits.addAll(getPossibleDeletions(word));
		edits.addAll(getPossibleInserts(word));
		edits.addAll(getPossibleReplacements(word));
		edits.addAll(getPossibleTranspositions(word));
		
		return edits;
	}

	public ArrayList<String> getPossibleDeletions(String word)
	{
		ArrayList<String> possibleSplits = new ArrayList<String>();
		for (int i = 0; i < word.length(); i++)
		{
			possibleSplits.add(word.substring(0, i) + word.substring(i+1));
		}

		return possibleSplits;
	}

	public ArrayList<String> getPossibleTranspositions(String word)
	{
		ArrayList<String> possibleDeletes = new ArrayList<String>();

		for(int i=0; i < word.length()-1; ++i) 
		{
			possibleDeletes.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		}

		return possibleDeletes;
	}

	public ArrayList<String> getPossibleReplacements(String word)
	{
		ArrayList<String> possibleAdditions = new ArrayList<String>();

		for(int i=0; i < word.length(); ++i) 
		{
			for(char c='a'; c <= 'z'; ++c) 
			{
				possibleAdditions.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
			}
		}

		return possibleAdditions;
	}

	public ArrayList<String> getPossibleInserts(String word)
	{
		ArrayList<String> possibleDeletions = new ArrayList<String>();

		for(int i=0; i <= word.length(); ++i) 
		{
			for(char c='a'; c <= 'z'; ++c)
			{
				possibleDeletions.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
			}
		}
		
		return possibleDeletions;
	}
	
	boolean isUrl(String inputWord)
	{
		Pattern pattern = Pattern.compile("(^http(s{0,1})://)?[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*");
		Matcher matcher = pattern.matcher(inputWord);
		
		return matcher.matches();
	}
	
	boolean isAtUsername(String inputWord)
	{
		Pattern pattern = Pattern.compile("@[a-zA-Z0-9_]+");
		Matcher matcher = pattern.matcher(inputWord);
		
		return matcher.matches();
	}
	
	boolean isHashtag(String inputWord)
	{
		Pattern pattern = Pattern.compile("#[a-zA-Z0-9_]+");
		Matcher matcher = pattern.matcher(inputWord);
		
		return matcher.matches();
	}
	
}