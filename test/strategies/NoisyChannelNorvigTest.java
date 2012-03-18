package strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

public class NoisyChannelNorvigTest 
{
	@Test
	public void testUrlMatcher()
	{
		NoisyChannelNorvig ncn = new NoisyChannelNorvig();
		
		assertFalse(ncn.isUrl("abc"));
		assertTrue(ncn.isUrl("http://google.com"));
		assertTrue(ncn.isUrl("http://tr.im/italiancoffee"));
		assertTrue(ncn.isUrl("google.com"));
	}
	
	@Test
	public void testAtUsernameMatcher()
	{
		NoisyChannelNorvig ncn = new NoisyChannelNorvig();
		
		assertFalse(ncn.isAtUsername("abc"));
		assertFalse(ncn.isAtUsername("@"));
		assertTrue(ncn.isAtUsername("@steven"));
		assertFalse(ncn.isAtUsername("@gmail.com"));
	}
	
	@Test
	public void testHashtag()
	{
		NoisyChannelNorvig ncn = new NoisyChannelNorvig();
		
		assertTrue(ncn.isHashtag("#ff"));
		assertFalse(ncn.isHashtag("abc"));
		assertFalse(ncn.isHashtag("nota#tag"));
	}
	
	@Test
	public void test()
	{
		String text = "this is 1 string of text";
		
		String[] words = text.split("\\s+"); // Split on 1 or more whitespace characters
		TreeMap<Character, Integer> treeMap = new TreeMap<Character, Integer>();
		
		if (text.length() == 0) // If we have an empty string then we don't need to ouput anything
		{
			System.out.println("Empty string");
			return;
		}
		
		for (int j = 0; j < words.length; j++)
		{
			for (int i = 0; i < text.length(); i++)
			{
				if (treeMap.get(text.charAt(i)) == null) // If the character is not already in the TreeMap then add it
				{			
					treeMap.put(text.charAt(i), 1);
				}
				else // If the character already exists increment its count
				{
					treeMap.put(text.charAt(i), (Integer) treeMap.get(text.charAt(i)) + 1);
				}
			}
		}
		
		Set set = treeMap.entrySet(); 
		// Get an iterator 
		Iterator i = set.iterator(); 
		// Display elements 
		while(i.hasNext()) { 
		Map.Entry me = (Map.Entry)i.next(); 
		System.out.print(me.getKey() + ": "); 
		System.out.println(me.getValue()); 
		} 
	}
}
