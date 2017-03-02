/**
 * 
 */
package com.ipmetric.p1.client.data;

import com.ipmetric.p1.client.ExtendedIterator;

class ExtendedIteratorImpl implements ExtendedIterator {
	private int currRow = 0;
	private int currCol = 0;
	private final Character[][] data;

	ExtendedIteratorImpl(final char[][] gridData) {
		data = new Character[gridData.length][];
		for (int i = 0; i < gridData.length; i++) {
			final char[] row = gridData[i];
			data[i] = new Character[row.length];
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = row[j];
			}
		}
	}

	public boolean hasMore() {
		return currCol < data[currRow].length;
	}

	public boolean hasNext() {
		return currRow < data.length - 1 || currCol < data[currRow].length;
	}

	public Character next() {
		if (currCol < data[currRow].length) return data[currRow][currCol++];
		currRow++;
		currCol = 0;
		return next();
	}

	public void remove() {
		next();
	}
}