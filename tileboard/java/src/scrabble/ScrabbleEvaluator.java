package scrabble;

/***
 * (C) Michah Lerner, 2009.  This code may be used for non-commercial purposes.
 * 
 * The game "Scrabble" is property of the Hasbro Company,
 * and should not be used for commercial purposes without
 * expressed written permission of the Hasbro Company.
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scrabble.IFEN.Axis;
import scrabble.util.Board;
import scrabble.util.BoardScorer;
import scrabble.util.BoardStart;
import scrabble.util.BoardTester;
import scrabble.util.Cell;
import scrabble.util.CellIterator;
import scrabble.util.Constants;
import scrabble.util.Line;
import scrabble.util.ScoredMove;
import scrabble.util.Utilities;
import scrabble.util.WildCarding;

public class ScrabbleEvaluator {

	private String dictionaryName = "dicts/SOWPODS.txt";
	private String boardName = "games/board.default";
	private int numResults = 10;
	final private Expander horizontalExpander = new Expander(this, Axis.HORIZONTAL);
	final private Expander verticalExpander = new Expander(this, Axis.VERTICAL);

	public Board board;
	public char[] rack;
	public Collection<String> vocab;

	public final List<ScoredMove> scoreList;
	public final BoardTester boardTester;
	public final BoardScorer boardScorer;
	public final WildCarding wildcardMatcher;
	public final Map<Cell, Character> wildcardValues;

	public ScrabbleEvaluator() {
		boardTester = new BoardTester(this);
		boardScorer = new BoardScorer(this);
		wildcardMatcher = new WildCarding(this);
		scoreList = new ArrayList<ScoredMove>();
		wildcardValues = new HashMap<Cell, Character>();
	}

	public boolean checkLexicon(final Line line) {
		final String text = line.toString(board).toUpperCase();
		if (text.length() == 0) return false;
		if (text.length() == 1) return true;
		return wildcardMatcher.checkWildcardedWord(text);
	}

	int rackAvail() {
		int num = 0;
		for (final char ch : rack)
			if (ch != Constants.EMPTY) {
				num++;
			}
		return num;
	}

	int evalOne(final Cell cell) {
		final int v1 = horizontalExpander.applyOperatorSequences(0, cell);
		final int v2 = verticalExpander.applyOperatorSequences(0, cell);
		return Math.max(v1, v2);
	}

	void evalAll() {
		board.updateShadow();
		final CellIterator ic = new CellIterator(board);
		while (ic.hasNext()) {
			final Cell cell = ic.next();
			if (board.isEmpty(cell) && board.hasAdjacent(cell)) {
				for (int i = 0; i < rack.length; i++) {

					final char piece = rack[i];
					if (piece == Constants.EMPTY) {
						continue;
					}

					rack[i] = Constants.EMPTY;
					board.setCell(cell, Character.toLowerCase(piece));

					evalOne(cell);

					board.setCell(cell, Constants.EMPTY);
					rack[i] = piece;
				}
			}
		}
	}

	static void usage() {
		System.out.println("Usage: java -jar scrabble.jar [-d dictionary] [-b board] rack ");
		System.out.println("          rack is the letters on the \"rack\" without spaces");
		System.out.println("          dictionary is the filename that contains the valid words");
		System.out.println("          each board is a (nomimally) 15x15 grid of uppercase characters.");
		System.out.println(" To test [broken] scoring: ");
		System.out.println("       java -jar scrabble.jar test board r# c# v|h wordToTest");
		System.out.println(" To find starting words:");
		System.out.println("       java -jar scrabble.jar start dictionary rack");
	}

	private char[] processArgs(final Iterator<String> argIterator) {
		String arg;
		while (true) {
			arg = argIterator.next();
			System.out.println("arg=" + arg);
			if (arg.equals("-a")) {
				numResults = Integer.parseInt(argIterator.next());
				System.out.println("numResults=" + numResults);
				continue;
			}
			if (arg.equals("-d")) {
				dictionaryName = argIterator.next();
				System.out.println("dictionaryName=" + dictionaryName);
				continue;
			}
			if (arg.equals("-b")) {
				boardName = argIterator.next();
				System.out.println("boardName=" + boardName);
				continue;
			}
			break;
		}
		final String rackText = arg.toUpperCase();
		System.out.println("rackText=" + rackText);
		return rackText.toCharArray();
	}

	static void testOrStart(final String[] args) {
		final BoardStart boardStart = new BoardStart();
		if (args[0].equals("-test")) {
			boardStart.test(Arrays.asList(args).iterator());
			return;
		}
		if (args[0].equals("-start")) {
			boardStart.start(args);
			return;
		}
	}

	static void showResults(final ScrabbleEvaluator constraintHierarchy) {
		Set<ScoredMove> tmp = new HashSet<ScoredMove>(constraintHierarchy.scoreList);
		final List<ScoredMove> res = new ArrayList<ScoredMove>(tmp);
		tmp = null;
		Collections.sort(res);

		if (constraintHierarchy.numResults != 0) {
			for (int i = Math.max(0, res.size() - constraintHierarchy.numResults); i < res.size(); i++) {
				System.out.printf("%06d) score=%04d [%17s] word=%s\n", i, res.get(i).getScore(), res.get(i).getLine(), res.get(i)
						.getWord().toLowerCase());
				System.out.println(constraintHierarchy.board.showUpdatedBoard(res.get(i).getWord(), res.get(i).getLine()));
			}
		}
	}

	static void readInputs(final ScrabbleEvaluator constraintHierarchy, final Iterator<String> argIterator) {
		constraintHierarchy.rack = constraintHierarchy.processArgs(argIterator);
		System.out.println("Rack: " + Arrays.toString(constraintHierarchy.rack));

		System.out.print("Reading vocab from  ... " + constraintHierarchy.dictionaryName);
		constraintHierarchy.vocab = Utilities.readInput(constraintHierarchy.dictionaryName);
		System.out.println("Vocabulary contains " + constraintHierarchy.vocab.size() + " entries.");

		System.out.println("Reading board from ... " + constraintHierarchy.boardName);
		constraintHierarchy.board = new Board(constraintHierarchy.boardName);

	}

	public static void main(final String[] args) {

		if (args.length == 0 || args[0].equals("-h")) {
			usage();
			return;
		}

		if (args[0].startsWith("-test") || args[0].startsWith("-start")) {
			testOrStart(args);
			return;
		}

		final Iterator<String> argIterator = Arrays.asList(args).iterator();
		final ScrabbleEvaluator constraintHierarchy = new ScrabbleEvaluator();
		readInputs(constraintHierarchy, argIterator);

		System.out.println("Starting Board\n" + constraintHierarchy.board.toString());

		constraintHierarchy.evalAll();

		showResults(constraintHierarchy);

		System.out.println("Generated " + BoardTester.numPasses + " non-unique results.");
	}
}
