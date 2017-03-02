/**
 * 
 */
package scrabble.util;

import scrabble.ScrabbleEvaluator;
import scrabble.IFEN.Axis;

public class BoardScorer {

	/**
	 * 
	 */
	private final ScrabbleEvaluator constraintHierarchy;

	/**
	 * @param constraintHierarchy
	 */
	public BoardScorer(final ScrabbleEvaluator constraintHierarchy) {
		this.constraintHierarchy = constraintHierarchy;
	}

	int partialScore(final Axis axis, final Line maxLine) {
		int score = 0;
		int sideScore = 0;
		int r0, rN, c0, cN;
		int primaryRstep, primaryCstep;
		int primaryR, primaryC;
		int scoreMultiplier = 1;
		r0 = maxLine.getR0();
		rN = maxLine.getRN();
		c0 = maxLine.getC0();
		cN = maxLine.getCN();

		primaryR = r0;
		primaryC = c0;
		int maxR, maxC;

		if (axis == Axis.HORIZONTAL) {
			maxR = primaryR;
			maxC = cN;
			primaryRstep = 0;
			primaryCstep = 1;
		} else {
			maxR = rN;
			maxC = c0;
			primaryRstep = 1;
			primaryCstep = 0;
		}

		do {
			final char ch = this.constraintHierarchy.board.getCell(primaryR, primaryC);
			final boolean newPiece = this.constraintHierarchy.board.isNew(primaryR, primaryC);
			int chScore = Utilities.getValueForChar(Character.toUpperCase(ch));
			int sideScoreIncrement = computeSideScore(axis, new Cell(primaryR, primaryC));
			if (newPiece) {
				switch (Utilities.boardBasis[primaryR][primaryC]) {
					case TW:
						scoreMultiplier *= 3;
						sideScoreIncrement *= 3;
						break;
					case TL:
						if (sideScoreIncrement != 0) {
							sideScoreIncrement += (2 * chScore);
						}
						chScore *= 3;
						break;
					case DW:
						scoreMultiplier *= 2;
						sideScoreIncrement *= 2;
						break;
					case DL:
						if (sideScoreIncrement != 0) {
							sideScoreIncrement += chScore;
						}
						chScore *= 2;
						break;
					case RG:
				}
				sideScore += sideScoreIncrement;
			}
			//sideScore += sideScoreIncrement;
			score += chScore;
			primaryR += primaryRstep;
			primaryC += primaryCstep;
		} while (primaryR <= maxR && primaryC <= maxC);
		score = score * scoreMultiplier + sideScore;
		return score;
	}

	int computeSideScore(final Axis axis, final Cell cell) {
		final Line dual = axis.getDual(cell);
		final int length = dual.length();
		if (length == 1) return 0;
		char sideChar;
		int score = 0, colStep = 0, rowStep = 0;
		final int r0 = dual.getR0(), rN = dual.getRN(), c0 = dual.getC0(), cN = dual.getCN();
		int row = r0, col = c0;
		if (axis == Axis.HORIZONTAL) {
			rowStep = 1;
		} else {
			colStep = 1;
		}
		do {
			sideChar = this.constraintHierarchy.board.getCell(row, col);
			final int letterValue = Utilities.getValueForChar(Character.toUpperCase(sideChar));
			score += letterValue;
			row += rowStep;
			col += colStep;
		} while (row <= rN && col <= cN);
		return score;
	}

}