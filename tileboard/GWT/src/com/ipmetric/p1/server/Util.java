package com.ipmetric.p1.server;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Util {
	static int insertBefore(final List<String> listA, final List<String> listB) {
		if (listA == null || listA.size() == 0) return 1;
		if (listB == null || listB.size() == 0) return -1;
		final Iterator<String> itA = listA.iterator();
		final Iterator<String> itB = listB.iterator();
		while (itA.hasNext() && itB.hasNext()) {
			final String stringA = itA.next();
			final String stringB = itB.next();
			final int cmp = stringA.compareTo(stringB);
			if (cmp != 0) return cmp;
		}
		if (!itA.hasNext() && !itB.hasNext()) return 0;
		if (!itA.hasNext()) return 1;
		return -1;
	}

	public static void mergeLists(final List<List<String>> mainList, final List<List<String>> newItems) {
		if (mainList == null || mainList.size() == 0) return;
		if (newItems == null || newItems.size() == 0) return;
		final ListIterator<List<String>> oldItemIterator = mainList.listIterator();
		final Iterator<List<String>> newitemIterator = newItems.iterator();
		List<String> oldRow = oldItemIterator.next();
		List<String> newRow = newitemIterator.next();
		do {
			final int cmp = insertBefore(oldRow, newRow);
			if (cmp == 0) {
				if (!newitemIterator.hasNext()) {
					break;
				}
				newRow = newitemIterator.next();
			} else if (cmp > 0) {
				oldItemIterator.previous();
				oldItemIterator.add(newRow);
				if (!newitemIterator.hasNext()) {
					break;
				}
				newRow = newitemIterator.next();
			} else {
				if (!oldItemIterator.hasNext()) {
					break;
				}
				oldRow = oldItemIterator.next();
			}
		} while (oldItemIterator.hasNext() && newitemIterator.hasNext());
		while (newitemIterator.hasNext()) {
			oldItemIterator.add(newitemIterator.next());
		}
	}
}
