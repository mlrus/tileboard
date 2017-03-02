/**
 * 
 */
package scrabble.util;

import java.util.Arrays;

import scrabble.ScrabbleEvaluator;

public class WildCarding {

	final static char[] validChars = Constants.VALIDCHARS.toCharArray();

	/**
	 * 
	 */
	private final ScrabbleEvaluator constraintHierarchy;

	/**
	 * @param constraintHierarchy
	 */
	public WildCarding(final ScrabbleEvaluator constraintHierarchy) {
		this.constraintHierarchy = constraintHierarchy;
	}

	public boolean checkWildcardedWord(final String text) {
		final int pos = text.indexOf('_');
		if (pos < 0) return this.constraintHierarchy.vocab.contains(text);
		final char[] textChars = text.toCharArray();
		boolean result = false;
		for (final char c : validChars) {
			textChars[pos] = c;
			final String charString = String.valueOf(textChars);
			if (this.constraintHierarchy.vocab.contains(charString)) {
				result = true;
			}
		}
		return result;
	}

	boolean traceScore = false;

	char[] getWildMatches(final String text) {
		return getWildMatches(text, validChars);
	}

	char[] getWildMatches(final String text, final char[] candidateCharacters) {
		final char[] mask = new char[26];
		int maskSize = 0;
		final int pos = text.indexOf('_');
		if (pos < 0) return new char[0];
		final char[] textChars = text.toCharArray();
		for (final char c : validChars) {
			textChars[pos] = c;
			final String charString = String.valueOf(textChars);
			if (this.constraintHierarchy.vocab.contains(charString)) {
				mask[maskSize++] = c;
			}
		}
		return Arrays.copyOf(mask, maskSize);
	}

	public char[] findWildcardValues(final String wordText, final Line secondaryAxis) {
		char[] matches;
		matches = getWildMatches(wordText);
		if (secondaryAxis.length() > 1 && matches.length > 1) {
			matches = getWildMatches(secondaryAxis.toString(this.constraintHierarchy.board).toUpperCase(), matches);
		}
		return matches;
	}
}