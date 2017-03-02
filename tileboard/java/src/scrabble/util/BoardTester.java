/**
 * 
 */
package scrabble.util;

import java.util.Arrays;

import scrabble.ScrabbleEvaluator;
import scrabble.IFEN.Axis;

public class BoardTester {
	static boolean debug = false;
	public static int numPasses = 0;
	/**
	 * 
	 */
	private final ScrabbleEvaluator constraintHierarchy;

	/**
	 * @param constraintHierarchy
	 */
	public BoardTester(final ScrabbleEvaluator constraintHierarchy) {
		this.constraintHierarchy = constraintHierarchy;
	}

	int testBoard(final Axis axis, final Cell cell, final Line dual) {
		final Line l = axis.getLine(cell);
		return testBoard(axis, l, dual);
	}

	public int testBoard(final Axis axis, final Line maxLine, final Line dual) {
		String wordText = "";
		String auxText = "";
		char[] wildMatches = new char[0];
		Cell wildcardCell = null;
		int score, wildcardPos;

		wordText = maxLine.toString(this.constraintHierarchy.board).toUpperCase();
		if (debug) {
			System.out.println(" testword[" + maxLine.toString() + "]=" + wordText);
		}

		if (!checkPerpendicular(axis, dual)) {
			if (debug) {
				System.out.println("Perpendicularity ng");
			}
			return 0;
		}

		if ((wildcardPos = wordText.indexOf('_')) < 0) {
			if (!this.constraintHierarchy.vocab.contains(wordText)) {
				if (debug) {
					System.out.println(": not a word");
				}
				return 0;
			}
		} else {
			wildMatches = this.constraintHierarchy.wildcardMatcher.findWildcardValues(wordText, dual);
			if (wildMatches.length == 0) return 0;
			wildcardCell = new Cell(dual.getR0(), dual.getC0() + wildcardPos);
			if (debug) {
				System.out.println("Wildcardmatch at " + wildcardCell);
			}
		}
		score = this.constraintHierarchy.boardScorer.partialScore(axis, maxLine);
		if (wildcardCell != null) {
			constraintHierarchy.wildcardValues.put(wildcardCell, wildMatches[0]);
			auxText = "  [" + Arrays.toString(wildMatches) + "]";
		}

		numPasses++;

		final ScoredMove scoredMove = new ScoredMove(this.constraintHierarchy, score, wordText + auxText, maxLine.clone());
		this.constraintHierarchy.scoreList.add(scoredMove);
		return score;
	}

	boolean checkPerpendicular(final Axis axis, final Line dual) {

		int len;
		len = dual.length();
		return len == 1 || constraintHierarchy.checkLexicon(dual);
	}
}