/**
 * 
 */
package scrabble.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import scrabble.ScrabbleEvaluator;
import scrabble.IFEN.Axis;
import scrabble.util.combinatoric.P;

public class BoardStart {
	/***
	 * Method start finds the best starting word (Currently ignores the double letter score in the middle)
	 * 
	 * @param args
	 */
	public void start(final String[] args) {
		// start dictionary rack
		System.out.println("Computing start words for dictionary " + args[1] + " and rack " + args[2]);
		final Collection<String> voc = Utilities.readInput(args[1]);
		System.out.println("voc has " + voc.size() + " words.");
		final P<String> p = new P<String>(voc);
		final List<String> items = new ArrayList<String>();
		for (final char c : args[2].toUpperCase().toCharArray()) {
			items.add(Character.toString(c));
		}
		final Collection<String> areWords = p.areWords(items);
		final List<String> res = new ArrayList<String>();
		for (final String s : areWords) {
			int wordScore = 0;
			for (final char ch : s.toCharArray()) {
				final int chScore = Utilities.getValueForChar(Character.toUpperCase(ch));
				wordScore += chScore;
			}
			res.add(String.format("%10s %s", wordScore, s));
		}
		Collections.sort(res);
		System.out.println("Got " + res.size() + " start words.");
		for (final String s : res) {
			System.out.println(s);
		}
		return;
	}

	public void test(final Iterator<String> argIterator) {
		argIterator.next();
		while (argIterator.hasNext()) {
			final ScrabbleEvaluator constraintHierarchy = new ScrabbleEvaluator();
			constraintHierarchy.board = new Board(argIterator.next());
			constraintHierarchy.board.updateShadow();
			final int row = Integer.parseInt(argIterator.next());
			final int col = Integer.parseInt(argIterator.next());
			final Axis axis = argIterator.next().charAt(0) == 'h' ? Axis.HORIZONTAL : Axis.VERTICAL;
			final char[] word = argIterator.next().toCharArray();
			int w = 0;
			if (axis.equals(Axis.HORIZONTAL)) {
				for (int c = col; c < 15; c++) {
					if (w < word.length) {
						constraintHierarchy.board.setCell(new Cell(row, c), word[w++]);
					}
				}
			} else {
				for (int r = row; r < 15; r++) {
					if (w < word.length) {
						constraintHierarchy.board.setCell(new Cell(r, col), word[w++]);
					}
				}
			}
			final Cell cell = new Cell(row, col);
			final Line maxLine = axis.getLine(cell);
			System.out.println("axis:" + axis + ";  maxLine=" + maxLine.toString(constraintHierarchy.board));
			final int score = constraintHierarchy.boardScorer.partialScore(axis, maxLine);
			System.out.println("score=" + score);
		}
	}
}