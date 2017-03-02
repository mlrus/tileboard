/**
 * 
 */
package scrabble.util;

import scrabble.IFEN.Direction;

public class Cell {
	final public static Cell NOSUCHCELL = new Cell(-1, -1);

	final private int row, col;

	// Upper left is row zero column zero

	public Cell(final int row, final int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public Cell(final Cell pos) {
		super();
		this.row = pos.row;
		this.col = pos.col;
	}

	@Override
	public String toString() {
		return "<" + row + "," + col + ">";
	}

	int getLeft() {
		return col - 1;
	}

	int getRight() {
		return col + 1;
	}

	int getUp() {
		return row - 1;
	}

	int getDown() {
		return row + 1;
	}

	Cell newShiftedCell(final Direction d) {
		final int r = row + d.getAdjacentRow();
		final int c = col + d.getAdjacentColumn();
		final Cell newPos = new Cell(r, c);
		return newPos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

//	@Override
//	public boolean equals(final Object obj) {
//		if (this == obj) return true;
//		if (obj == null) return false;
//		if (getClass() != obj.getClass()) return false;
//		final Cell other = (Cell) obj;
//		if (col != other.col) return false;
//		if (row != other.row) return false;
//		return true;
//	}

	public final int getRow() {
		return row;
	}

	public final int getCol() {
		return col;
	}

}