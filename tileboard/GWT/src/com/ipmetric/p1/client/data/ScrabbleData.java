package com.ipmetric.p1.client.data;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.ipmetric.p1.client.BoardChangeListener;
import com.ipmetric.p1.client.BoardChangeNotifier;
import com.ipmetric.p1.client.Rack;
import com.ipmetric.p1.client.Util;

public class ScrabbleData implements BoardChangeNotifier {
	public final static int nRow = 15, nCol = 15;
	private BoardChangeListener boardChangeListener;

	BoardData boardData;
	RackData rackContent;
	WordInfo wordInfo;

	String wordToPlay;
	int row;
	int col;
	Axis axis;

	public enum Axis {
		HORIZONTAL, VERTICAL, UNDEFINED;
	}

	public ScrabbleData() {
		this(new char[nRow][nCol], Rack.unknownRack, new WordInfo());
	}

	ScrabbleData(final char[][] boardContent, final RackData rackData, final WordInfo wordInfo) {
		GWT.log("ScrabbleData::setBoardContent", null);
		this.setBoardData(new BoardData(boardContent));
		//this.boardContent = boardContent;
		this.rackContent = rackData;
		this.wordInfo = wordInfo;
		this.wordToPlay = "";
		this.axis = Axis.UNDEFINED;
		this.row = -1;
		this.col = -1;
	}

	public ScrabbleData(final List<String> cs, final String rackContent, final WordInfo wordInfo) {
		this(Util.toArrArr(cs), new RackData(rackContent), wordInfo);
	}

	public RackData getRack() {
		return rackContent;
	}

	void setBoardContent(final char[][] boardContent) {
		getBoardData().set(boardContent);
		GWT.log("boardContent.length=" + boardContent.length, null);
		if (boardChangeListener != null) {
			GWT.log("calling boardChangeListener.onChange", null);
			boardChangeListener.onChange(this);
			GWT.log("called boardChangeListener.onChange", null);
		}
	}

	void setBoardContent(final List<String> boardContent) {
		setBoardContent(Util.toArrArr(boardContent));
	}

	void setRackData(final String rackContent) {
		this.setRackContent(rackContent);
	}

	void setWordToPlay(final String wordToPlay) {
		this.wordToPlay = wordToPlay;
	}

	void setLoc(final int row, final int col, final Axis axis) {
		this.row = row;
		this.col = col;
		this.axis = axis;
	}

	void playWord(final String word, final int r, final int c, final Axis a) {
		setWordToPlay(word);
		setLoc(r, c, a);
	}

	public void addChangeListener(final BoardChangeListener listener) {
		GWT.log("addChangeListener(boardChangeListener)" + listener, null);
		this.boardChangeListener = listener;
	}

	public void setBoardData(final BoardData boardData) {
		this.boardData = boardData;
	}

	public BoardData getBoardData() {
		return boardData;
	}

	public WordInfo getWordInfo() {
//		if (wordInfo.rowCount() == 0) { // add special dummy if no data, to get widget width
//			wordInfo.addWord();
//		}
		return wordInfo;
	}

	public void setRackContent(final String rackContent) {
		this.rackContent = new RackData(rackContent);
	}

	public String getRackContent() {
		return rackContent.getRack();
	}

}
