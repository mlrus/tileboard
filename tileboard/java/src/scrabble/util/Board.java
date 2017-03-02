/**
 * 
 */
package scrabble.util;

import java.util.Arrays;
import java.util.Iterator;

import scrabble.IFEN.Direction;

public class Board implements Cloneable {

	static private Board instance;

	static public int getStaticDimension() {
		return instance == null ? 0 : instance.dimension;
	}

	final int dimension;
	char[][] cellData;
	final char[][] shadowData;

	@Override
	protected Board clone() {
		final Board newBoard = new Board(this.cellData.length);
		for (int i = 0; i < this.cellData.length; i++) {
			newBoard.cellData[i] = Arrays.copyOf(this.cellData[i], this.cellData[i].length);
		}
		return newBoard;
	}

	Board(final int dim) {
		dimension = dim;
		this.cellData = new char[dim][];
		this.shadowData = new char[dim][];
		for (int i = 0; i < dim; i++) {
			this.shadowData[i] = new char[dim];
		}
		for (int i = 0; i < dim; i++) {
			this.cellData[i] = new char[dim];
			Arrays.fill(this.cellData[i], '\u0000');
		}
		if (instance == null) {
			instance = this;
			Line.setBoardInstance(instance);
		}
	}

	public Board(final String fn) {
		this(ReaderContainer.getInstanceIterator(fn), ReaderContainer.getDimension());
	}

	private Board(final Iterator<String> rowdata, final int dim) {
		this.dimension = dim;
		cellData = new char[dim][];
		this.shadowData = new char[dim][];
		for (int i = 0; i < dim; i++) {
			this.shadowData[i] = new char[dim];
		}
		int row = 0;
		while (rowdata.hasNext()) {
			cellData[row++] = Arrays.copyOf(rowdata.next().toCharArray(), dim);
		}
		cleanBoard();
		if (instance == null) {
			instance = this;
			Line.setBoardInstance(instance);
		}
	}

	private void cleanBoard() {
		for (final char[] row : cellData) {
			for (int col = 0; col < row.length; col++)
				if (Constants.VALID.indexOf(row[col]) < 0) {
					row[col] = '\u0000';
				}
		}
	}

	public void updateShadow() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				shadowData[i][j] = this.cellData[i][j];
			}
		}
	}

	public boolean isNew(final int r, final int c) {
		return this.cellData[r][c] != shadowData[r][c];
	}

	public boolean isEmpty(final Cell p) {
		return testCell(p, Constants.EMPTY);
	}

	public boolean isNotEmpty(final Cell p) {
		return !testCell(p, Constants.EMPTY);
	}

	public boolean isEmpty(final Cell p, final Direction d) {
		final int r = p.getRow() + d.getAdjacentRow();
		if (r < 0 || r >= dimension) return false;
		final int c = p.getCol() + d.getAdjacentColumn();
		if (c < 0 || c >= dimension) return false;
		return cellData[r][c] == Constants.EMPTY;
	}

	public boolean isNotEmpty(final Cell p, final Direction d) {
		final int r = p.getRow() + d.getAdjacentRow();
		if (r < 0 || r >= dimension) return false;
		final int c = p.getCol() + d.getAdjacentColumn();
		if (c < 0 || c >= dimension) return false;
		return cellData[r][c] != Constants.EMPTY;
	}

	public boolean isInvalid(final Cell p) {
		final int r = p.getRow();
		final int c = p.getCol();
		return r < 0 || r >= dimension || c < 0 || c >= dimension;
	}

	public boolean isValid(final Cell p, final Direction d) {
		final int r = p.getRow() + d.getAdjacentRow();
		if (r < 0 || r >= dimension) return true;
		final int c = p.getCol() + d.getAdjacentColumn();
		if (c < 0 || c >= dimension) return true;
		return false;
	}

	public boolean isInvalid(final char c) {
		return c == Constants.INVALID;
	}

	private boolean testCell(final Cell p, final char c) {
		return getCell(p) == c;
	}

	public char getCell(final Cell p) {
		if (p.getRow() >= cellData.length || p.getCol() >= cellData.length) {
			System.out.println("GETCELL BOUND ERR: " + p);
			return '\u0000';
		}
		return cellData[p.getRow()][p.getCol()];
	}

	public char getCell(final int row, final int col) {
		return cellData[row][col];
	}

	public char getCell(final Cell p, final Direction d) {
		final int r = p.getRow() + d.getAdjacentRow();
		final int c = p.getCol() + d.getAdjacentColumn();
		if (r < 0 || c < 0 || r >= dimension || c >= dimension) return Constants.INVALID;
		return cellData[r][c];
	}

	public boolean setCell(final Cell p, final char c) {
		if (p.getRow() >= cellData.length || p.getCol() >= cellData.length) {
			System.out.println("PUTCELL BOUND ERR: " + p);
			return false;
		}
		cellData[p.getRow()][p.getCol()] = c;
		return true;
	}

	public boolean hasAdjacent(final Cell p) {
		for (final Direction d : Direction.values()) {
			if (isNotEmpty(p, d)) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		int row = 1;
		final StringBuffer sb = new StringBuffer("    ABCDEFGHIJKLMNO\n");
		for (final char[] element : cellData) {
			sb.append(String.format("%02d |", row++));
			for (final char element2 : element) {
				sb.append(element2 == '\u0000' ? " " : element2);
			}
			sb.append("|\n");
		}
		sb.append("  =================\n");
		return sb.toString();
	}

	public String showUpdatedBoard(final String word, final Line line) {
		//Copy data (a pig, but only done at the end of the output to show the results)
		final char[][] newCellData = Arrays.copyOf(this.cellData, this.cellData.length);
		for (int i = 0; i < newCellData.length; i++) {
			newCellData[i] = Arrays.copyOf(this.cellData[i], this.cellData.length);
		}
		final char[][] realCellData = this.cellData;
		cellData = newCellData;
		// Overwrite
		final char[] priorValue = new char[word.length()];
		final char[] newValue = word.toLowerCase().toCharArray();
		String result = null;
		Cell cell = new Cell(line.getR0(), line.getC0());
		for (int c = 0; c < word.length(); c++) {
			priorValue[c] = getCell(cell);
			setCell(cell, newValue[c]);
			cell = cell.newShiftedCell(line.getAxis().getTailDirection());
		}
		//Stringify
		result = this.toString();
		cell = new Cell(line.getR0(), line.getC0());
		for (int c = 0; c < word.length(); c++) {
			setCell(cell, priorValue[c]);
			cell = cell.newShiftedCell(line.getAxis().getTailDirection());
		}
		//Restore
		cellData = realCellData;
		return result;
	}
}