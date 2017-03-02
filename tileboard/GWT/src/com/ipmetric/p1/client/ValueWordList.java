/**
 * 
 */
package com.ipmetric.p1.client;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.ipmetric.p1.client.data.ScrabbleData.Axis;

class ValueWordList extends Composite {
	/**
	 * 
	 */
	private final WordList wordList;
	String[] priorTexts;
	boolean[] priorEnabled;
	String[] priorFormats;

	ValueWordList(final WordList wordList) {
		this.wordList = wordList;
		final HorizontalPanel hp = new HorizontalPanel();
		this.initWidget(hp);

	}

	// WordList is a container with a widget.  
	// Values is a list of strings, expected to be word, startCell and value
	ValueWordList(final WordList wordList, final Iterator<String> values) {
		this.wordList = wordList;
		final HorizontalPanel hp = new HorizontalPanel();
		this.initWidget(hp);
		while (values.hasNext()) {
			final Label label = new Label(values.next());
			label.addMouseOverHandler(mouseOverHandler);
			label.addMouseOutHandler(mouseOutHandler);
			label.addClickHandler(clickHandler);
			label.setStylePrimaryName(this.wordList.contentFmts[hp.getWidgetCount() % this.wordList.contentFmts.length]);
			hp.add(label);
		}
	}

	String getWord() {
		final HorizontalPanel hp = (HorizontalPanel) this.getWidget();
		return ((Label) (hp.getWidget(1))).getText();
	}

	String getStartcell() {
		final HorizontalPanel hp = (HorizontalPanel) this.getWidget();
		return ((Label) (hp.getWidget(2))).getText();
	}

	Axis getAxis() {
		return getAxis(getStartcell());
	}

	Axis getAxis(final String startCell) {
		final char ch = startCell.charAt(0);
		return (ch >= 'A' && ch <= 'Z') ? Axis.VERTICAL : Axis.HORIZONTAL;
	}

	int getRow() {
		return getRow(getStartcell(), getAxis());
	}

	int getRow(final String startCell, final Axis axis) {
		if (axis == Axis.VERTICAL) return Integer.parseInt(startCell.substring(1)) - 1;
		final String subs = startCell.substring(0, startCell.length() - 1);
		final int i = Integer.parseInt(subs);
		return i - 1;
	}

	int getCol() {
		return getCol(getStartcell(), getAxis());
	}

	int getCol(final String startCell, final Axis axis) {
		if (axis == Axis.VERTICAL) return startCell.charAt(0) - 'A';
		return startCell.charAt(startCell.length() - 1) - 'A';

	}

	String describeText() {
		final HorizontalPanel hp = (HorizontalPanel) this.getWidget();
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hp.getWidgetCount(); i++) {
			sb.append(((Label) hp.getWidget(i)).getText() + " : ");
		}
		return sb.substring(0, sb.length() - 3);
	}

	void showSelection() {
		final HorizontalPanel hp = (HorizontalPanel) this.getWidget();
		for (int i = 0; i < hp.getWidgetCount(); i++) {
			final Label label = (Label) hp.getWidget(i);
			label.setStylePrimaryName(this.wordList.contentSHOWFmts[i % this.wordList.contentSHOWFmts.length]);
		}
	}

	void unShowSelection() {
		final HorizontalPanel hp = (HorizontalPanel) this.getWidget();
		for (int i = 0; i < hp.getWidgetCount(); i++) {
			final Label label = (Label) hp.getWidget(i);
			label.setStylePrimaryName(this.wordList.contentFmts[i % this.wordList.contentFmts.length]);
		}
	}

	MouseOverHandler mouseOverHandler = new MouseOverHandler() {
		public void onMouseOver(final MouseOverEvent mouseOverEvent) {
			getCurrentWord();
			showSelection();
			setWord("gridSHOW");
		}
	};

	MouseOutHandler mouseOutHandler = new MouseOutHandler() {
		public void onMouseOut(final MouseOutEvent mouseOutEvent) {
			unShowSelection();
			restoreWord();
		}
	};

	ClickHandler clickHandler = new ClickHandler() {
		public void onClick(final ClickEvent event) {
			setWord();
			priorTexts = null;
			priorEnabled = null;
			priorFormats = null;
		}
	};

	void setWord() {
		setWord("gridSET");
	}

	void setWord(final String style) {
		final String word = getWord();
		final String startCell = getStartcell();
		final Axis axis = getAxis(startCell);
		int row = getRow(startCell, axis);
		int col = getCol(startCell, axis);

		for (int i = 0; i < word.length(); i++) {
			final LabelItem cell = (LabelItem) this.wordList.scrabbleGrid.gameBoard.boardGrid.getWidget(row, col);
			cell.setEnabled(true);
			cell.setText(String.valueOf(word.charAt(i)));
			this.wordList.scrabbleGrid.gameBoard.boardData.setTile(row, col, word.charAt(i));
			cell.setEnabled(false);
			cell.setStylePrimaryName(style);
			cell.setEnabled(false);
			if (axis == Axis.VERTICAL) {
				row++;
			}
			if (axis == Axis.HORIZONTAL) {
				col++;
			}
		}
	}

	void restoreWord() {
		if (priorTexts != null) {
			final String startCell = getStartcell();
			final Axis axis = getAxis(startCell);
			int row = getRow(startCell, axis);
			int col = getCol(startCell, axis);
			for (int i = 0; i < priorTexts.length; i++) {
				final LabelItem cell = (LabelItem) this.wordList.scrabbleGrid.gameBoard.boardGrid.getWidget(row, col);
				this.wordList.scrabbleGrid.gameBoard.boardData.setTile(row, col, priorTexts[i].charAt(0));
				// Restore text, primaryStyle and enabled status.
				cell.setEnabled(true);
				cell.setText(priorTexts[i]);
				cell.setStylePrimaryName(priorFormats[i]);
				cell.setEnabled(priorEnabled[i]);

				if (axis == Axis.VERTICAL) {
					row++;
				}
				if (axis == Axis.HORIZONTAL) {
					col++;
				}
			}
		}
	}

	String getCurrentWord() {
		final String word = getWord(); // acquire as many characters as it has;
		priorEnabled = new boolean[word.length()];
		priorFormats = new String[word.length()];
		priorTexts = new String[word.length()];
		final char[] currentWord = new char[word.length()];

		final String startCell = getStartcell();
		final Axis axis = getAxis(startCell);
		int row = getRow(startCell, axis);
		int col = getCol(startCell, axis);
		for (int i = 0; i < word.length(); i++) {
			final LabelItem cell = (LabelItem) this.wordList.scrabbleGrid.gameBoard.boardGrid.getWidget(row, col);
			final String currentValue = cell.getText();
			currentWord[i] = ' ';
			priorTexts[i] = currentValue;
			priorEnabled[i] = cell.isEnabled();
			priorFormats[i] = cell.getStylePrimaryName();
			if (currentValue != null && currentValue.length() > 0) {
				final char ch = currentValue.charAt(0);
				currentWord[i] = ch;
			}

			if (axis == Axis.VERTICAL) {
				row++;
			}
			if (axis == Axis.HORIZONTAL) {
				col++;
			}
		}
		final String result = String.valueOf(currentWord);
		return result;
	}
}