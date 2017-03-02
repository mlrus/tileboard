package scrabble.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/***
 * Class ReaderContainer implements multiple valued static return by locking the singleton object until all required
 * values have been read by the one valid instance. The clone method ensures keeps these separate.
 * 
 * @author mlrus
 * 
 */
public class ReaderContainer {

	static private final ReaderContainer instance = new ReaderContainer();
	static private STATE state = STATE.free;
	static private ArrayList<String> content;
	static private int dimension;

	private ReaderContainer() {
	}

	@SuppressWarnings("unchecked")
	static public Iterator<String> getInstanceIterator(final String fn) {
		try {
			instance.waitForInstance();
			content = new ArrayList<String>();
			readInput(fn, content, true);
			dimension = content.size();
			return ((AbstractList<String>) content.clone()).iterator();
		} catch (final InterruptedException e) {
			instance.releaseInstance();
			e.printStackTrace();
		}
		return null;
	}

	static public int getDimension() {
		final int dim = dimension;
		instance.releaseInstance();
		return dim;
	}

	static private enum STATE {
		free, inuse
	};

	private void releaseInstance() {
		synchronized (this) {
			state = STATE.free;
			notifyAll();
		}
	}

	private void waitForInstance() throws InterruptedException {
		while (true) {
			synchronized (this) {
				if (state == STATE.free) {
					state = STATE.inuse;
					return;
				}
				wait();
			}
		}
	}

	static private final void readInput(final String fn, final Collection<String> container, final boolean toUpperCase) {
		BufferedReader bufferedReader = null;
		final File file = new File(fn);
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			do {
				final String s = CleanForCharset.cleanString(bufferedReader.readLine());
				if (s == null) {
					continue;
				}
				container.add(toUpperCase ? s.toUpperCase() : s);
			} while (bufferedReader.ready());
		} catch (final Exception e) {
			System.err.println("Could not read: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public final static Collection<String> readInput(final String fn) {
		final Set<String> vocab = new HashSet<String>();
		readInput(fn, vocab, false);
		vocab.remove("");
		return vocab;
	}

}
