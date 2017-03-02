package com.ipmetric.p1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.ipmetric.p1.client.data.BoardData;
import com.ipmetric.p1.client.data.GameState;
import com.ipmetric.p1.client.data.RackData;
import com.ipmetric.p1.client.data.ScrabbleData;
import com.ipmetric.p1.client.data.TestData;
import com.ipmetric.p1.client.data.WordInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Wscr implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	final private MServiceAsync callProvider = GWT.create(MService.class);

	final private TestData testData = new TestData();

	final ScrabbleWidget[] scrabbleGrids = new ScrabbleWidget[testData.numTests()];

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {
		ScrabbleData scrabbleData = new ScrabbleData(testData.getBoard(0), testData.getRack(0), testData.getWordInfo(0));
		scrabbleData = new ScrabbleData();
		final ScrabbleWidget scrabbleWidget = new ScrabbleWidget("board.0", scrabbleData, this);
		RootPanel.get("board.0").add(scrabbleWidget);
	}

	void loadWidget(final String boardName, final ScrabbleWidget scrabbleWidget) {
		callProvider.retrieveGame(boardName, new AsyncCallback<GameState>() {
			public void onFailure(final Throwable caught) {
				Window.alert("RPC to retrieveGame() failed.");
			}

			public void onSuccess(final GameState result) {
				final BoardData boardData = result.getBoardData();
				final RackData rackData = result.getRackData();
				GWT.log("new rackData is " + rackData.getRack(), null);
				scrabbleWidget.rack.updateRack(rackData);
				scrabbleWidget.gameBoard.updateBoard(boardData);

			}
		});
	}

	public void solve(final BoardData boardData, final RackData rack, final ScrabbleWidget scrabbleWidget) {
		callProvider.solve(boardData, rack, new AsyncCallback<WordInfo>() {
			public void onFailure(final Throwable caught) {
				Window.alert("RPC to solve() failed.");
			}

			public void onSuccess(final WordInfo result) {
				scrabbleWidget.gameBoard.updateBoard(boardData);
				final RackData rackData = new RackData("?????");
				scrabbleWidget.rack.updateRack(rackData);
				scrabbleWidget.wordList.setWordlist(result);
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						scrabbleWidget.infoPanel.setWidth((scrabbleWidget.gameBoard.getOffsetWidth()) + "px");
					}
				});
			}
		});
	}
}
