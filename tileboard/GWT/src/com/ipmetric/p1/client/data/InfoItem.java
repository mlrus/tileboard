/**
 * 
 */
package com.ipmetric.p1.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InfoItem implements Iterable<String>, Comparable<InfoItem>, IsSerializable {
	List<String> dat;
	int val;

	InfoItem() {
		dat = Arrays.asList(new String[] { " ", " ", " " });
		val = 0;
	}

	InfoItem(final String value, final String text, final String loc) {
		dat = new ArrayList<String>(3);
		final String s = value.trim();
		dat.add(s);

		val = Integer.parseInt(s);
		dat.add(text.trim());
		dat.add(loc.trim());
	}

	String getValue() {
		return dat.get(0);
	}

	String getText() {
		return dat.get(1);
	}

	String getLoc() {
		return dat.get(2);
	}

	public Iterator<String> iterator() {
		return dat.iterator();
	}

	public int compareTo(final InfoItem o) {
		if (this.val != o.val) return this.val < o.val ? 1 : -1;
		final int cmp = dat.get(1).compareTo(o.dat.get(1));
		if (cmp != 0) return cmp;
		return dat.get(2).compareTo(o.dat.get(2));
	}

	@Override
	public String toString() {
		return getText() + " @" + getLoc() + " " + getValue() + " points";
	}
}