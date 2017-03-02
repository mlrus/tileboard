package com.ipmetric.p1.client;

import java.util.List;

public class Util {

	public static char toUpper(final char ch) {
		if (ch >= 'a' && ch <= 'z') return (char) ('A' + ch - 'a');
		return ch;
	}

	public static boolean validChar(final char ch) {
		return ch == ' ' || (ch >= 'A' && ch <= 'Z');
	}

	public static char[][] toArrArr(final List<String> cs) {
		final char[][] res = new char[cs.size()][];
		int r = 0;
		for (final String s : cs) {
			res[r] = new char[s.length()];
			for (int i = 0; i < s.length(); i++) {
				res[r][i] = s.charAt(i);
			}
			r++;
		}
		return res;
	}
}
