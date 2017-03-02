/**
 * 
 */
package scrabble;

import scrabble.IFEN.Axis;
import scrabble.IFEN.Direction;
import scrabble.util.Cell;
import scrabble.util.Constants;
import scrabble.util.Line;

class Expander {
	/**
	 * 
	 */
	private final ScrabbleEvaluator constraintHierarchy;
	private final Axis axis;
	private final Direction[] expansionDirections;

	Expander(final ScrabbleEvaluator constraintHierarchy, final Axis axis) {
		this.constraintHierarchy = constraintHierarchy;
		this.axis = axis;
		this.expansionDirections = axis.getExtensionDirections();
	}

	private int d = 0;

	int applyOperatorSequences(final int nextExpanderIndex, final Cell cell) {
		d++;
		int numInRack = this.constraintHierarchy.rackAvail();
		int val = Integer.MIN_VALUE;
		final Line dual = axis.getDual(cell);
		if (dual.length() == 1 || this.constraintHierarchy.checkLexicon(dual)) {
			final Line maxLine = axis.getLine(cell);
			for (int expanderIndex = nextExpanderIndex; expanderIndex < expansionDirections.length; expanderIndex++) {
				if (numInRack > 0) {
					final Cell expandLoc = maxLine.expandLine(expansionDirections[expanderIndex]);
					if (expandLoc != Cell.NOSUCHCELL && this.constraintHierarchy.board.hasAdjacent(expandLoc)) {
						final Line wrdLine = axis.getLine(expandLoc);
						final Line dualLoc = axis.getDual(expandLoc);
						for (int rackPos = 0; rackPos < this.constraintHierarchy.rack.length; rackPos++) {
							final char ch = this.constraintHierarchy.rack[rackPos];
							//numInRack--;
							if (ch == Constants.EMPTY) {
								continue;
							}
							numInRack--;
							this.constraintHierarchy.rack[rackPos] = Constants.EMPTY;
							this.constraintHierarchy.board.setCell(expandLoc, Character.toUpperCase(ch));
							val = this.constraintHierarchy.boardTester.testBoard(axis, wrdLine, dualLoc);

							if (numInRack > 0) {
								int newVal = applyOperatorSequences(nextExpanderIndex, expandLoc);
								val = Math.max(val, newVal);
								if (nextExpanderIndex < expansionDirections.length - 1) {
									newVal = applyOperatorSequences(nextExpanderIndex + 1, expandLoc);
									val = Math.max(val, newVal);
								}
							}
							this.constraintHierarchy.rack[rackPos] = ch;
							numInRack++;
						}
						this.constraintHierarchy.board.setCell(expandLoc, Constants.EMPTY);
						maxLine.contractLine(expansionDirections[expanderIndex]);
					}
				}
			}
			d--;
			return val;
		}
		d--;
		return 0;
	}
}