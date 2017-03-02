package com.ipmetric.p1.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GameState implements IsSerializable {
	BoardData boardData;
	RackData rackData;

	GameState() {
		boardData = new BoardData();
		rackData = new RackData();
	}

	public GameState(final BoardData boardData, final RackData rackData) {
		this.boardData = boardData;
		this.rackData = rackData;
	}

	public BoardData getBoardData() {
		return boardData;
	}

	public RackData getRackData() {
		return rackData;
	}
}
