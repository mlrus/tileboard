package com.ipmetric.p1.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ipmetric.p1.client.data.BoardData;
import com.ipmetric.p1.client.data.GameState;
import com.ipmetric.p1.client.data.RackData;
import com.ipmetric.p1.client.data.WordInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("m2")
public interface MService extends RemoteService {

	GameState retrieveGame(String name);

	WordInfo solve(BoardData boardData, RackData rackData);

	String greetServer(String name);

	String getParam(String paramName);

}
