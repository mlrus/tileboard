package com.ipmetric.p1.client;

import com.ipmetric.p1.client.data.ScrabbleData.Axis;

public class Constants {

	private static Axis defaultAxis = Axis.VERTICAL;
	private static boolean defaultWrap = false;

	public final static int nRow = 15, nCol = 15;

	public static Axis getDefaultAxis() {
		return defaultAxis;
	}

	public static boolean getDefaultWrap() {
		return defaultWrap;
	}

}
