package com.ipmetric.p1.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ipmetric.p1.client.Constants;
import com.ipmetric.p1.client.ExtendedIterable;
import com.ipmetric.p1.client.ExtendedIterator;

public class BoardData implements ExtendedIterable, IsSerializable {

	char[][] tiles;

	BoardData() {
		tiles = new char[Constants.nRow][];
		for (int i = 0; i < Constants.nRow; i++) {
			tiles[i] = new char[Constants.nCol];
			Arrays.fill(tiles[i], ' ');
		}
	}

	public BoardData(final char[][] boardData) {
		tiles = boardData;
	}

	int nRows() {
		return tiles.length;
	}

	public char getTile(final int row, final int col) {
		return tiles[row][col];
	}

	public void setTile(final int row, final int col, final char ch) {
		tiles[row][col] = ch;
	}

	public void set(final char[][] boardContent) {
		if (tiles == boardContent) //GWT.log("Skip set set", null);
			return;
		for (int i = 0; i < tiles.length; i++) {
			System.arraycopy(boardContent[i], 0, tiles[i], 0, tiles[i].length);
		}
	}

	public void update(final BoardData boardData) {
		set(boardData.tiles);
	}

	public ExtendedIterator iterator() {
		return new ExtendedIteratorImpl(tiles);
	}

	public List<String> getContent() {
		final List<String> result = new ArrayList<String>();
		for (final char[] tile : tiles) {
			result.add(String.valueOf(tile));
		}
		return result;
	}

	public String getContentString() {
		final StringBuffer sb = new StringBuffer();
		for (final String string : getContent()) {
			sb.append(string + "\n");
		}
		return sb.toString();
	}

}
