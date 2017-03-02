package com.ipmetric.p1.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ipmetric.p1.client.data.BoardData;
import com.ipmetric.p1.client.data.GameState;
import com.ipmetric.p1.client.data.RackData;
import com.ipmetric.p1.client.data.WordInfo;

/**
 * The async counterpart of <code>MService</code>.
 */
public interface MServiceAsync {

	void greetServer(String input, AsyncCallback<String> callback);

	void getParam(java.lang.String paramName, com.google.gwt.user.client.rpc.AsyncCallback<java.lang.String> arg2);

	void solve(BoardData boardData, RackData rackData, AsyncCallback<WordInfo> asyncCallback);

	void retrieveGame(String name, AsyncCallback<GameState> callback);

}
