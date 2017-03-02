package com.ipmetric.p1.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ipmetric.p1.client.data.ScrabbleData;

public class ScrabbleWidget extends HorizontalPanel {

	final GameBoard gameBoard;
	final WordList wordList;
	final Rack rack;
	final Button solveButton;
	final Button loadButton;

	final ScrabbleData data;
	final ScrabbleControl controller;

	Panel infoPanel;

	/***
	 * Define call provider with updateWidget method
	 */

	ScrabbleWidget() {
		this("grid");
	}

	ScrabbleWidget(final String name) {
		this(name, new ScrabbleData(), new Wscr());
	}

	ScrabbleWidget(final String name, final ScrabbleData scrabbleData, final Wscr wScr) {
		super();
		this.data = scrabbleData;
		this.controller = new ScrabbleControl(this.data);
		this.gameBoard = new GameBoard(name, data.getBoardData());
		this.wordList = new WordList(this, data.getWordInfo());
		this.rack = new Rack(data.getRack());
		final ScrabbleWidget scrabbleWidget = this;

		this.solveButton = new Button("<b>solve</b>", new ClickHandler() {
			public void onClick(final ClickEvent event) {
				wScr.solve(data.getBoardData(), data.getRack(), scrabbleWidget);
			}
		});

		this.loadButton = new Button("<b>load</b>", new ClickHandler() {
			public void onClick(final ClickEvent event) {
				wScr.loadWidget("board", scrabbleWidget);
			}
		});

		data.addChangeListener(new BoardChangeListener() {
			public void onChange(final ScrabbleData d) {
				GWT.log("ScrabbleWidget::addedChangeListener::onChange(data)", null);
			}
		});

		this.add(defaultLayout());
	}

	Panel defaultLayout() {
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.add(gap("5px"));
		final Panel boardAndRack = layoutBoardAndRack();
		panel.add(boardAndRack);
		panel.add(gap());
		panel.add(wordList);
		panel.add(gap("5px"));
		return panel;
	}

	Panel layoutBoardAndRack() {
		final VerticalPanel boardAndInfo = new VerticalPanel();
		boardAndInfo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		infoPanel = layoutInfo();
		infoPanel.setWidth("300px");

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				infoPanel.setWidth((gameBoard.getOffsetWidth()) + "px");
			}
		});

		boardAndInfo.add(infoPanel);
		boardAndInfo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		boardAndInfo.add(gameBoard);
		return boardAndInfo;
	}

	Panel layoutInfo() {
		final HorizontalPanel info = new HorizontalPanel();
		info.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		info.add(solveButton);
		info.add(loadButton);
		info.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		info.add(rack);
		return info;
	}

	private Panel gap() {
		return gap("25px");
	}

	private Panel gap(final String gap) {
		final VerticalPanel space = new VerticalPanel();
		space.setWidth(gap);
		return space;
	}

}
