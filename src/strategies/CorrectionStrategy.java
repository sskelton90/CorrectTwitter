package strategies;

import java.util.HashMap;

public interface CorrectionStrategy {
	
	public String findCorrection(String inputWord, HashMap<String, Integer> dictionaryWords);

}
