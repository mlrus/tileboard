package com.ipmetric.p1.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.ipmetric.p1.client.data.BoardData;
import com.ipmetric.p1.client.data.ScrabbleData;
import com.ipmetric.p1.client.data.ScrabbleData.Axis;

public class GameBoard extends Composite implements BoardChangeListener {

	final BoardData boardData;
	final String name;
	final Grid boardGrid;

	Axis prevailingAxis;

	GameBoard(final String name, final BoardData boardData) {
		this.name = name;
		this.boardGrid = mkBoardGrid();
		this.boardData = boardData;
		this.setContentView();
	}

	public void updateBoard(final BoardData data) {
		boardData.update(data);
		setContentView();
	}

	final private void setContentView() {
		final ExtendedIterator dataIterator = boardData.iterator();
		final Iterator<Widget> cellIterator = boardGrid.iterator();
		while (dataIterator.hasNext()) {
			final LabelItem label = (LabelItem) cellIterator.next();
			final Character ch = dataIterator.next();
			if (Util.validChar(ch)) {
				label.setText(ch.toString());
				label.setStylePrimaryName("gridUSE");
				label.setEnabled(false);
			} else {
				label.setWidth("18px");
			}
		}
	}

	final private Grid mkBoardGrid() {
		final Grid grid = new Grid(Constants.nRow, Constants.nCol);
		initWidget(grid);
		grid.setStylePrimaryName("gridTable");
		final Iterator<SquareType> it = board.iterator();
		int row = 0, col = 0;
		while (it.hasNext() && row < Constants.nRow) {
			final SquareType sq = it.next();
			final LabelItem labelitem = new LabelItem(this, sq.getLabel(), row, col);
			labelitem.setStylePrimaryName(sq.getFormat());
			grid.setWidget(row, col, labelitem);
			col++;
			if (col >= Constants.nCol) {
				col = 0;
				row++;
			}
		}
		return grid;
	}

	static enum SquareType {
		_TW, _DW, _TL, _DL, _RG;
		final static String ccsBase = "grid";

		String getFormat() {
			return ccsBase + toString().substring(1);
		}

		String getLabel() {
			if (this.equals(RG)) return " ";
			return toString().substring(1);
		}
	};

	static final SquareType TW = SquareType._TW;
	static final SquareType DW = SquareType._DW;
	static final SquareType TL = SquareType._TL;
	static final SquareType DL = SquareType._DL;
	static final SquareType RG = SquareType._RG;

	static List<SquareType> board = Arrays.asList(new SquareType[] {

	TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW,

	RG, DW, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, DW, RG,

	RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG,

	DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL,

	RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG,

	RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG,

	RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG,

	TW, RG, RG, DL, RG, RG, RG, DW, RG, RG, RG, DL, RG, RG, TW,

	RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG,

	RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG,

	RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG,

	DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL,

	RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG,

	RG, DW, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, DW, RG,

	TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW

	});

	public void onChange(final ScrabbleData scrabbleData) {
		GWT.log("onChange(scrabbleData)", null);
	}
}
