/**
 * 
 */
package com.ipmetric.p1.client;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.ipmetric.p1.client.data.InfoItem;
import com.ipmetric.p1.client.data.WordInfo;

class WordList extends VerticalPanel {
	/**
	 * 
	 */
	final ScrabbleWidget scrabbleGrid;
	final Grid wordGrid;
	final RowFormatter rowFormatter;
	final private HorizontalPanel labelPanel;
	private final String[] labelFormats = new String[] { "wordLabelVal", "wordLabelText", "wordLabelLoc" };
	final String[] contentFmts = new String[] { "wordListVal", "wordListText", "wordListLoc" };
	final String[] contentSHOWFmts = new String[] { "wordListValSHOW", "wordListTextSHOW", "wordListLocSHOW" };

	WordList(final ScrabbleWidget scrabbleGrid) {
		super();
		this.scrabbleGrid = scrabbleGrid;
		this.setWidth("128px");
		labelPanel = mkLabelPanel("Best Words");
		this.add(labelPanel);
		wordGrid = new Grid();
		wordGrid.setBorderWidth(1);
		wordGrid.setWidth("12px");
		rowFormatter = wordGrid.getRowFormatter();
		final ScrollPanel scrollPanel = new ScrollPanel(wordGrid);
		scrollPanel.setAlwaysShowScrollBars(false);
		this.add(scrollPanel);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				wordGrid.setHeight((scrabbleGrid.gameBoard.getOffsetHeight()) + "px");
				scrollPanel.setSize((20 + wordGrid.getOffsetWidth()) + "px", (scrabbleGrid.gameBoard.getOffsetHeight()) + "px");
			}
		});
	}

	public WordList(final ScrabbleWidget scrabbleWidget, final WordInfo wordInfo) {
		this(scrabbleWidget);
		setWordlist(wordInfo);
	}

	HorizontalPanel mkLabelPanel(final String text) {
		final HorizontalPanel p = new HorizontalPanel();
		p.add(new InlineLabel(text));
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		p.setStylePrimaryName("wordLabel");
		return p;
	}

	void setWordlist(final WordInfo wordInfo) {
		relabel(wordInfo.names);
		wordGrid.resize(wordInfo.rowCount(), 1);
		final Iterator<InfoItem> it = wordInfo.getIterator();
		int row = -1;
		while (it.hasNext()) {
			row++;
			final Iterator<String> values = it.next().iterator();
			final ValueWordList vwl = new ValueWordList(this, values);
			wordGrid.setWidget(row, 0, vwl);
			rowFormatter.setStyleName(row, getRowStyle(row));
		}
	}

	private String getRowStyle(final int row) {
		final boolean even = row % 2 == 0;
		String style = "";
		if (even == true) {
			style = "rs-even";
		} else {
			style = "rs-odd";
		}
		return style;
	}

	private void relabel(final String[] colNames) {
		labelPanel.clear();
		final Grid lGrid = new Grid(1, colNames.length);
		int lterm = 0;
		for (final String colName : colNames) {
			final Label lbl = new Label(colName);
			lbl.setTitle(colName);
			lbl.setStylePrimaryName(labelFormats[lterm]);
			lGrid.setWidget(0, lterm++, lbl);
		}
		labelPanel.add(lGrid);
	}
}