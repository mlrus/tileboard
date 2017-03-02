package com.ipmetric.p1.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WordInfo implements IsSerializable {
	final static String[] defaultNames = new String[] { "val", "text", "cell" };

	public String[] names;
	boolean changed = false;
	ArrayList<InfoItem> data;

	WordInfo() {
		this.names = defaultNames;
		data = new ArrayList<InfoItem>();
		addWord();
	}

	public WordInfo(final String... names) {
		this.names = names;
		data = new ArrayList<InfoItem>();
	}

	public Iterator<InfoItem> getIterator() {
		if (changed) {
			Collections.sort(data);
			changed = false;
		}
		return data.iterator();
	}

	public int rowCount() {
		return data.size();
	}

	public int colCount() {
		return data.get(0).dat.size();
	}

	public void addWord(final String s) {
		final String[] vwg = s.trim().split(" +", 3);
		addWord(vwg[1], vwg[0], vwg[2]);
	}

	public void addWord(final String word, final String value, final String gridLoc) {
		changed = true;
		data.add(new InfoItem(value, word, gridLoc));
	}

	public void addWord() { // special case of no data, needs a dummy to get width in widget
		data.add(new InfoItem("0", " - ", "A0"));
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("results[" + data.size() + "]=\n");
		final Iterator<InfoItem> it = getIterator();
		while (it.hasNext()) {
			sb.append("  " + it.next().toString() + "\n");
		}
		return sb.toString();
	}

}
