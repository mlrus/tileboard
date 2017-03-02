package com.ipmetric.p1.server;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cache support overriding the standard super of the BoundedLinkedHashMap
 * 
 * @author Michah.Lerner
 * 
 * @param <K>
 *            Type of key
 * @param <V>
 *            Type of value
 */
public class BoundedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	/**
         *
         */
	private static final long serialVersionUID = 7010322958620795621L;
	private static final int MAX_ENTRIES = 128;
	private static final int SIZE = 32;
	private final int maxEntries;

	public BoundedLinkedHashMap(final int size, final float factor, final boolean b) {
		this(size, factor, b, MAX_ENTRIES);
	}

	public BoundedLinkedHashMap(final int size, final float factor, final boolean b, final int maxEntries) {
		super(size, factor, b);
		this.maxEntries = maxEntries;
	}

	public BoundedLinkedHashMap() {
		this(SIZE, 0.75f, true);
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return size() > maxEntries;
	}
}
