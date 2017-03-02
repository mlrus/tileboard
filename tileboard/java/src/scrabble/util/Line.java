package scrabble.util;

import scrabble.IFEN.Axis;
import scrabble.IFEN.Direction;

public class Line {

	int r0;
	int rN;
	int c0;
	int cN;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c0;
		result = prime * result + cN;
		result = prime * result + r0;
		result = prime * result + rN;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Line other = (Line) obj;
		if (c0 != other.c0) return false;
		if (cN != other.cN) return false;
		if (r0 != other.r0) return false;
		if (rN != other.rN) return false;
		return true;
	}

	public final int getR0() {
		return r0;
	}

	public final int getRN() {
		return rN;
	}

	public final int getC0() {
		return c0;
	}

	public final int getCN() {
		return cN;
	}

	protected static Board staticBoard;

	private static Line instance = new Line();

	public synchronized static void setBoardInstance(final Board board) {
		staticBoard = board;
	}

	// The following static methods are synchronized because they rely on the instance.

	public synchronized static Line getMaxRow(final Cell cell) {
		return getInstance(cell).getMaxRow(staticBoard);
	}

	public synchronized static Line getMaxCol(final Cell cell) {
		return getInstance(cell).getMaxCol(staticBoard);
	}

	private static Line getInstance(final Cell cell) {
		instance.r0 = cell.getRow();
		instance.c0 = cell.getCol();
		instance.rN = instance.r0;
		instance.cN = instance.c0;
		return instance;
	}

	final boolean testAssertions() {
		assert r0 <= rN && c0 <= cN;
		assert r0 == rN || c0 == cN;
		assert r0 >= 0 && rN < Board.getStaticDimension() && c0 >= 0 && cN < Board.getStaticDimension();
		return true;
	}

	private Line() {

	}

	@Override
	public Line clone() {
		return new Line(this);
	}

	public Line(final Line line) {
		this(line.r0, line.c0, line.rN, line.cN);
	}

	public Line(final Cell from, final Cell to) {
		this(from.getRow(), from.getCol(), to.getRow(), to.getCol());
	}

	public Line(final int r0, final int c0, final int rN, final int cN) {
		testAssertions();
		this.r0 = r0;
		this.c0 = c0;
		this.rN = rN;
		this.cN = cN;
	}

	public Line(final int r, final int c) {
		this.r0 = r;
		this.rN = r;
		this.c0 = c;
		this.cN = c;
	}

	public Line(final Cell cell) {
		this(cell.getRow(), cell.getCol());
	}

	@Override
	public String toString() {
		if (r0 == rN && c0 != cN) return String.format("row%02d cols[%02d:%02d]", r0, c0, cN);
		if (c0 == cN && r0 != rN) return String.format("col%02d rows[%02d:%02d]", c0, r0, rN);
		return String.format("cell[%02d:%02d]", r0, c0);
	}

	public int length() {
		return length(staticBoard);
	}

	public int length(final Board b) {
		if (b.cellData[r0][c0] == Constants.EMPTY) return 0;
		final int len = rN - r0 + cN - c0 + 1;
		return len;
	}

	public Axis getAxis() {
		if (r0 != rN) return Axis.VERTICAL;
		if (c0 != cN) return Axis.HORIZONTAL;
		return Axis.UNDEFINED;
	}

	public Cell expandLine(final Direction d) {
		return expandLine(staticBoard, d);
	}

	public Cell expandLine(final Board b, final Direction d) {
		assert c0 == cN || r0 == rN;
		switch (d) {
			case LEFT:
				if (c0 == 0) return Cell.NOSUCHCELL;
				c0--;
				return new Cell(r0, c0);

			case RIGHT:
				if (cN >= b.dimension - 1) return Cell.NOSUCHCELL;
				cN++;
				return new Cell(r0, cN);

			case UP:
				if (r0 == 0) return Cell.NOSUCHCELL;
				r0--;
				return new Cell(r0, c0);

			case DOWN:
			default:
				if (rN >= b.dimension - 1) return Cell.NOSUCHCELL;
				rN++;
				return new Cell(rN, c0);
		}
	}

	public Cell contractLine(final Direction d) {
		return contractLine(staticBoard, d);
	}

	public Cell contractLine(final Board b, final Direction d) {
		assert c0 == cN || r0 == rN;
		switch (d) {
			case LEFT:
				c0++;
				return new Cell(r0, c0);

			case RIGHT:
				cN--;
				return new Cell(r0, cN);

			case UP:
				r0++;
				return new Cell(r0, c0);

			case DOWN:
			default:
				rN--;
				return new Cell(rN, c0);
		}
	}

	void showRow(final int row, final Board board) {
		System.out.println("       0123456789ABCDEF");
		System.out.printf("row%02d:", row);
		for (int i = 0; i < board.dimension; i++) {
			char c = board.cellData[row][i];
			if (c == Constants.EMPTY) {
				c = ' ';
			}
			System.out.print(c);
		}
		System.out.println();
	}

	void showCol(final int col, final Board board) {
		System.out.print("col " + col + " : ");
		for (int i = 0; i < board.dimension; i++) {
			char c = board.cellData[i][col];
			if (c == Constants.EMPTY) {
				c = ' ';
			}
			System.out.print(c);
		}
		System.out.println();
	}

	public Line getMaxRow(final Board board) {
		assert r0 == rN;
		int leftCol = c0;
		int rightCol = cN;

		while (leftCol > 0 && board.cellData[r0][leftCol - 1] != Constants.EMPTY) {
			leftCol--;
		}

		while (rightCol < board.dimension - 1 && board.cellData[r0][rightCol + 1] != Constants.EMPTY) {
			rightCol++;
		}

		final Line newLine = new Line(r0, leftCol, rN, rightCol);
		return newLine;
	}

	public Line getMaxCol(final Board board) {
		assert c0 == cN;
		int topRow = r0;
		int bottomRow = rN;

		while (topRow > 0 && board.cellData[topRow - 1][c0] != Constants.EMPTY) {
			topRow--;
		}
		while (bottomRow < board.dimension - 1 && board.cellData[bottomRow + 1][c0] != Constants.EMPTY) {
			bottomRow++;
		}

		final Line newLine = new Line(topRow, c0, bottomRow, cN);
		return newLine;
	}

	private char replaceEmpty(final char c) {
		return c == Constants.EMPTY ? '#' : c;
	}

	public String toString(final Board b) {
		final StringBuffer sb = new StringBuffer();
		switch (getAxis()) {
			case HORIZONTAL:
				for (int col = c0; col <= cN; col++) {
					sb.append(replaceEmpty(b.cellData[r0][col]));
				}
				break;
			case VERTICAL:
				for (int row = r0; row <= rN; row++) {
					sb.append(replaceEmpty(b.cellData[row][c0]));
				}
				break;
			case UNDEFINED:
				if (this.length(b) == 0) {
					break;
				}
				sb.append(replaceEmpty(b.cellData[r0][c0]));
		}
		return sb.toString();
	}
}
