package scrabble.util;

import java.util.Iterator;

import scrabble.IFEN.BoardReader;

public class CellIterator implements Iterator<Cell>, BoardReader {
	final int dimension;
	boolean hasnext = true;
	int row = 0, col = -1;

	private CellIterator(final int dimension) {
		this.dimension = dimension;
	}

	public CellIterator(final Board b) {
		this(b.dimension);
	}

	public boolean hasNext() {
		return hasnext;
	}

	public Cell next() {

		if (col < dimension - 1) {
			col++;
		} else {
			col = 0;
			row++;
		}
		hasnext = row < dimension - 1 || col < dimension - 1;
		return new Cell(row, col);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
