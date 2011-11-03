package strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import wordtools.Dictionary;

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
}
