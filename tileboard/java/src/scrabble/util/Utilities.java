package scrabble.util;

/***
 * (C) Michah Lerner, 2009.  This code may be used for non-commercial purposes.
 * 
 * The game "Scrabble" is property of the Hasbro Company,
 * and should not be used for commercial purposes without
 * expressed written permission of the Hasbro Company.
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import scrabble.IFEN.BoardScores;

public class Utilities {

	final static BoardScores TW = BoardScores.TW;
	final static BoardScores TL = BoardScores.TL;
	final static BoardScores DL = BoardScores.DL;
	final static BoardScores DW = BoardScores.DW;
	final static BoardScores RG = BoardScores.RG;

	public static BoardScores[][] boardBasis = new BoardScores[][] {
			new BoardScores[] { TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW },
			new BoardScores[] { RG, DW, RG, RG, RG, TL, RG, TW, RG, TL, RG, RG, RG, DW, RG },
			new BoardScores[] { RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG },
			new BoardScores[] { DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL },
			new BoardScores[] { RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG },
			new BoardScores[] { RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG },
			new BoardScores[] { RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG },
			new BoardScores[] { TW, RG, RG, DL, RG, RG, RG, DW, RG, RG, RG, DL, RG, RG, TW },
			new BoardScores[] { RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG },
			new BoardScores[] { RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG },
			new BoardScores[] { RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG },
			new BoardScores[] { DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL },
			new BoardScores[] { RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG },
			new BoardScores[] { RG, DW, RG, RG, RG, TL, RG, TW, RG, TL, RG, RG, RG, DW, RG },
			new BoardScores[] { TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW } };

	private final static Map<Character, Integer> valueMap = (new Object() {
		public Map<Character, Integer> mkMap() {
			//A-1 B-3 C-3 D-2  E-1 F-4 G-2 H-4 I-1 J-8 K-5 L-1 M-3
			//N-1 O-1 P-3 Q-10 R-1 S-1 T-1 U-1 V-4 W-4 X-8 Y-4 Z-10
			final String valueString = "A=1 B=3 C=3 D=2 E=1 F=4 G=2 H=4 I=1 J=8 K=5 L=1 M=3 N=1 O=1 P=3 Q=10 R=1 S=1 T=1 U=1 V=4 W=4 X=8 Y=4 Z=10";
			final HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			for (final String s : valueString.split(" ")) {
				final String s2[] = s.split("=");
				map.put(s2[0].charAt(0), Integer.parseInt(s2[1]));
			}
			return Collections.unmodifiableMap(map);
		};
	}).mkMap();

	public final static int getValueForChar(final char x) {
		if (x == '_') return 0;
		final Integer v = valueMap.get(x);
		if (v == null) return 1;
		return v;
	}

	public final static int points(final CharSequence cs) {
		int total = 0;
		for (int i = 0; i < cs.length(); i++) {
			final Integer v = valueMap.get(cs.charAt(i));
			if (v != null) {
				total += v;
			} else {
				total += 1;
			}
		}
		return total;
	}

	public final static Collection<String> readInput(final String fn) {
		final Set<String> vocab = new HashSet<String>();
		readInput(fn, vocab, true);
		vocab.remove("");
		return vocab;
	}

	public final static Collection<String> readInput(final String fn, final Collection<String> container, final boolean toUpperCase) {
		BufferedReader q = null;
		try {
			q = new BufferedReader(new FileReader(fn));
			do {
				final String s = q.readLine();
				if (s == null) {
					continue;
				}
				container.add(toUpperCase ? s.toUpperCase(Locale.getDefault()) : s);
			} while (q.ready());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			if (q != null) {
				q.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return container;
	}

	static CharSequence mkSeq(final List<CharSequence> l) {
		final StringBuffer sb = new StringBuffer();
		for (final CharSequence c : l) {
			sb.append(c);
		}
		return sb.toString();
	}

	static List<CharSequence> mkSeq(final CharSequence in) {
		final List<CharSequence> dat = new ArrayList<CharSequence>();
		for (int i = 0; i < in.length(); i++) {
			dat.add(in.subSequence(i, i + 1));
		}
		return dat;
	}

}
