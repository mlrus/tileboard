/**
 * 
 */
package com.ipmetric.p1.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.ipmetric.p1.client.data.ScrabbleData.Axis;

class LabelItem extends TextBox {

	/**
	 * 
	 */
	private final GameBoard gameBoard;
	int row, col;
	String priorStyle;

	MouseOverHandler mouseOverHandler = new MouseOverHandler() {
		public void onMouseOver(final MouseOverEvent mouseOverEvent) {
			priorStyle = getStylePrimaryName();
			if (getText() == null || getText().length() != 1 || getText().equals(" ")) {
				setStylePrimaryName("gridOvercell");
			}
		}
	};

	MouseOutHandler mouseOutHandler = new MouseOutHandler() {
		public void onMouseOut(final MouseOutEvent mouseOutEvent) {
			setStylePrimaryName(priorStyle);
		}
	};

	LabelItem(final GameBoard gameBoard, final String text, final int row, final int col) {
		super();
		this.gameBoard = gameBoard;
		setText(text);
		this.gameBoard.prevailingAxis = Constants.getDefaultAxis();
		if (text == null || (text.length() == 1 && !text.equals(" "))) {
			//Blank
		} else {
			setStylePrimaryName("gridUSE");
		}

		this.row = row;
		this.col = col;
		this.addMouseOverHandler(mouseOverHandler);
		this.addMouseOutHandler(mouseOutHandler);
		this.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(final KeyDownEvent event) {
				final char ch = Util.toUpper((char) (event.getNativeKeyCode()));

				if (Util.validChar(ch)) {
					if (isEnabled()) {
						final String stringValue = String.valueOf(ch);
						setText(stringValue);
						gameBoard.boardData.setTile(row, col, ch);
						setStylePrimaryName("gridUSE");
						setEnabled(false);
					} else {
						setText(String.valueOf(gameBoard.boardData.getTile(row, col)));
					}
					nextEnabledItem();
				} else {
					setText(String.valueOf(gameBoard.boardData.getTile(row, col)));
					if (event.isDownArrow()) {
						gameBoard.prevailingAxis = Axis.VERTICAL;
					} else if (event.isUpArrow()) {
						gameBoard.prevailingAxis = Axis.VERTICAL;
					} else if (event.isLeftArrow()) {
						gameBoard.prevailingAxis = Axis.HORIZONTAL;
					} else if (event.isRightArrow()) {
						gameBoard.prevailingAxis = Axis.HORIZONTAL;
					} else {
						GWT.log("not valid char:" + ch + ".", null);
					}
				}
			}
		});
	}

	boolean nextEnabledItem() {
		return nextEnabledItem(Constants.getDefaultWrap());
	}

	boolean nextEnabledItem(final boolean wrap) {
		LabelItem nextItem = this;
		do {
			nextItem = nextItem(nextItem, wrap); //axis, wrap);
			if (nextItem == null) {
				GWT.log("Could not get enabled item for new focus.", null);
				return false;
			}
			if (nextItem.isEnabled()) {
				nextItem.setFocus(true);
				return true;
			}
		} while (nextItem != this);
		return false;
	}

	LabelItem nextItem(final LabelItem item, final boolean wrap) {
		int r = item.row;
		int c = item.col;
		switch (this.gameBoard.prevailingAxis) {
			case VERTICAL:
				r++;
				if (r >= Constants.nRow) {
					if (!wrap) return null;
					r = 0;
					c++;
				}
				break;
			case HORIZONTAL:
			default:
				c++;
				if (c >= Constants.nCol) {
					if (!wrap) return null;
					c = 0;
					r++;
				}
				break;
		}
		if (r >= Constants.nRow || col >= Constants.nCol) return null;
		final LabelItem nextItem = (LabelItem) this.gameBoard.boardGrid.getWidget(r, c);
		return nextItem;
	}

}