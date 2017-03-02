package com.ipmetric.p1.server;

import java.util.Properties;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ipmetric.p1.client.MService;
import com.ipmetric.p1.client.data.BoardData;
import com.ipmetric.p1.client.data.GameState;
import com.ipmetric.p1.client.data.RackData;
import com.ipmetric.p1.client.data.TestData;
import com.ipmetric.p1.client.data.WordInfo;

/**
 * The server side implementation of the RPC service.
 */

public class MServiceImpl extends RemoteServiceServlet implements MService {

	final Logger logger = Logging.getLogger();
	final private static long serialVersionUID = 1L;
	final private static PropertyInfo propertyInfo = new PropertyInfo();

	public MServiceImpl() {
		System.out.println("MServiceImpl()");
	}

	public String greetServer(final String input) {
		final String serverInfo = getServletContext().getServerInfo();
		final String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	public String getParam(final String paramName, final String defaultValue) {
		final Properties properties = getPropertyInfo().getProperties();
		return properties.getProperty(paramName, defaultValue);
	}

	public String getParam(final String paramName) {
		return getParam(paramName, null);
	}

	public static PropertyInfo getPropertyInfo() {
		return propertyInfo;
	}

	public WordInfo solve(final BoardData boardData, final RackData rackData) {
		System.out.println("Solve{(\n" + boardData.getContentString() + "),(" + rackData.getRack() + ")}");
		final WordInfo res = TestData.getTestData().getWordInfo(0);
		System.out.println(res.toString());
		return res;
	}

	public GameState retrieveGame(final String name) {
		System.out.println("Am retrieving game " + name);
		GWT.log("Am retrieving game", null);
		final BoardData boardData = new BoardData(com.ipmetric.p1.client.Util.toArrArr(TestData.getTestData().getBoard(0)));
		final RackData rackData = new RackData(TestData.getTestData().getRack(0));
		return new GameState(boardData, rackData);
	}
}
