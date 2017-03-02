package scrabble.util.combinatoric;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;

public class P<S> {

	Collection<S> vocab;

	public P(final Collection<S> vocab) {
		this.vocab = vocab;
	}

	public interface CGen<T> {
		/**
		 * Are there more possibilities to iterator over
		 * 
		 * @return true if there are more possibilities
		 */
		boolean hasNext();

		/**
		 * Get the next item, storing into the collection provided
		 * 
		 * @param resultVector
		 *            the collection to receive the result
		 * @return the resultVector with items added to it
		 */
		Collection<T> next(Collection<T> resultVector);

		Collection<T> next();

		Long nextCombID();

		Long maxCombID();

		String nextAsString(Collection<T> resultVector);

		String nextAsString();

		// padded versions (should be optional)
		Collection<T> next(Collection<T> resultVector, T pad);

		Collection<T> next(T pad);

		String nextAsString(Collection<T> resultVector, T pad);

		String nextAsString(T pad);

		long count();
	}

	public class Comb<T> implements CGen<T> {
		Long bits;
		Long maxVal;
		Long maxBit;
		T[] items;

		public Comb(final T[] items) {
			this.items = items;
			maxVal = 1L << items.length;
			maxBit = Long.valueOf(items.length);
			bits = 1L; // Don't need to see the empty set.
		}

		@SuppressWarnings("unchecked")
		public Comb(final Collection<T> itemList) {
			this(itemList.toArray((T[]) (new Object[itemList.size()])));
		}

		public Long nextCombID() {
			if (hasNext()) return bits;
			return -1L;
		}

		public Long maxCombID() {
			return maxVal - 1;
		}

		public boolean hasNext() {
			return bits < maxVal;
		}

		public Collection<T> next() {
			return next(new ArrayList<T>());
		}

		public String nextAsString() {
			return nextAsString(new ArrayList<T>());
		}

		// Padded versions accept an additional placeholder to show the skipped items
		public Collection<T> next(final Collection<T> result, final T pad) {
			for (int bitPos = 0; bitPos < maxBit; bitPos++) {
				if ((bits & (1L << bitPos)) != 0) {
					result.add(this.items[bitPos]);
				} else {
					result.add(pad);
				}
			}
			bits++;
			return result;
		}

		public Collection<T> next(final T pad) {
			return next(new ArrayList<T>(), pad);
		}

		public String nextAsString(final Collection<T> res, final T pad) {
			final Collection<T> result = next(res, pad);
			if (result == null) return null;
			final StringBuffer sb = new StringBuffer();
			for (final T s : result) {
				sb.append(s + " ");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		public String nextAsString(final T pad) {
			return nextAsString(new ArrayList<T>(), pad);
		}

		public long count() {
			return maxVal;
		}

		public Collection<T> next(final Collection<T> ans) {
			for (int bitPos = 0; bitPos < maxBit; bitPos++) {
				if ((bits & (1L << bitPos)) != 0) {
					ans.add(this.items[bitPos]);
				}
			}
			bits++;
			return ans;
		}

		public String nextAsString(final Collection<T> resVector) {
			final Collection<T> resultVector = next(resVector);
			if (resultVector == null) return null;
			final StringBuffer sb = new StringBuffer();
			for (final T s : resultVector) {
				sb.append(s + " ");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}
	}

	public class Perm<T> implements CGen<T> {
		// Perm.perm(n) == perm(rowID,n) ==> Nth permutation of rowID.
		// NOTE: caller must call next before calling these.
		ArrayList<T> items;
		long nextPerm;
		long maxPerm;

		Choose choose = new Choose(Arrays.asList(new Object[0]));

		/**
		 * For compatibility with combination,s this accepts collection and returns collections. Internally it still
		 * uses an array list. It probably should be called with an ordered input to preserve the permutations'
		 * distinction from combinations.
		 */
		Perm() {
			items = new ArrayList<T>();
			nextPerm = -1;
			maxPerm = 0;
		}

		Perm(final Collection<T> items) {
			this.items = new ArrayList<T>(items);
			this.maxPerm = choose.fact(this.items.size());
			this.nextPerm = 1;
		}

		Perm(final T[] items) {
			this(new ArrayList<T>(Arrays.asList(items)));
		}

		public boolean hasNext() {
			return nextPerm <= maxPerm;
		}

		public Collection<T> next() {
			return perm(nextPerm++);
		}

		public String nextAsString() {
			return permString(nextPerm++);
		}

		public String nextAsString(final Collection<T> p) {
			return permString(nextPerm++, p);
		}

		String permString(final long permNumb) {
			return permString(permNumb, new ArrayList<T>());
		}

		Collection<T> perm(final long permNumb) {
			return perm(permNumb, new ArrayList<T>());
		}

		@SuppressWarnings("unchecked")
		Collection<T> perm(final long pNumb, final Collection<T> answer) {
			long permNumb = pNumb;
			final ArrayList<T> tmp = (ArrayList<T>) this.items.clone();
			for (int i = this.items.size(); i > 0; i--) {
				final int item = (int) (permNumb % i);
				permNumb /= i;
				answer.add(tmp.remove(item));
			}
			return answer;
		}

		String permString(final long permNumb, final Collection<T> pstore) {
			final Collection<T> p = perm(permNumb, pstore);
			final StringBuffer sb = new StringBuffer();
			for (final T s : p) {
				sb.append(s + " ");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		public long count() {
			return maxPerm;
		}

		public Long maxCombID() {
			return maxPerm;
		}

		public ArrayList<T> next(final T pad) {
			throw new UnsupportedOperationException();
		}

		public String nextAsString(final T pad) {
			throw new UnsupportedOperationException();
		}

		public Long nextCombID() {
			throw new UnsupportedOperationException();
		}

		public Collection<T> next(final Collection<T> p) {
			return perm(nextPerm++, p);
		}

		public Collection<T> next(final Collection<T> resultVector, final T pad) {
			throw new UnsupportedOperationException();
		}

		public String nextAsString(final Collection<T> resultVector, final T pad) {
			throw new UnsupportedOperationException();
		}
	}

	public List<List<S>> permute(final List<S> l) {
		final List<List<S>> rl = new ArrayList<List<S>>();
		if (l.size() == 0) return null;
		if (l.size() == 1) {
			rl.add(l);
			return rl;
		}
		if (l.size() > 1) {
			for (int i = 0; i < l.size(); i++) {
				final S item = l.get(0);
				l.subList(0, 1).clear();
				final List<List<S>> p2 = permute(l);
				for (final List<S> scol : p2) {
					final List<S> res = new ArrayList<S>(scol);
					res.add(item);
					rl.add(res);
				}
				l.add(item);
			}
		}
		return rl;
	}

	class Choose {

		static final int _limit = 10;
		int limit = _limit;
		Random rgen = new Random();
		SecureRandom srgen = null;
		public ArrayList<Object> results = new ArrayList<Object>();
		public SortedMap<Integer, List<Object>> resultStore;
		Object[] items; // current combination being constructed
		Object[] data; // input info
		int M, N, P;

		public Choose(final Object[] l, final int m, final int n, final int limit) {
			this.limit = limit;
			if (l.length != m) return;
			this.M = m;
			this.N = n;
			this.P = 0;
			data = l;
			items = new Object[n];
			for (int i = 0; i < n; i++) {
				items[i] = "-";
			}
			choose(m, n);
		}

		Choose(final List<Object> l, final int m, final int n, final int limit) {
			this(l.toArray(), m, n, limit);
		}

		Choose(final Object[] l, final int m, final int n) {
			this(l, m, n, _limit);
		}

		Choose(final List<Object> l, final int m, final int n) {
			this(l, m, n, _limit);
		}

		Choose(final List<Object> l) {
			this(l, l.size(), l.size());
		}

		BigInteger bigFact(final BigInteger val) {
			if (val.compareTo(BigInteger.ONE) > 0) return (val.multiply(bigFact(val.subtract(BigInteger.ONE))));
			return BigInteger.ONE;
		}

		BigInteger bigFact(final int val) {
			return bigFact(new BigInteger(String.format("%d", val)));
		}

		BigInteger bigCombs(final BigInteger m, final BigInteger n) {
			return bigFact(m).divide(bigFact(n).divide(bigFact(m.subtract(n))));
		}

		BigInteger bigCombs(final int m, final int n) {
			return bigCombs(new BigInteger(String.format("%d", m)), new BigInteger(String.format("%d", n)));
		}

		long fact(final long val) {
			if (val > 0) return (val * fact(val - 1));
			return 1L;
		}

		long combs(final long m, final long n) {
			return fact(m) / (fact(n) / fact(m - n));
		}

		long combs(final int m, final int n) {
			return combs((long) m, (long) n);
		}

		void secureShuffle(final Object[] a) {
			try {
				if (srgen == null) {
					srgen = SecureRandom.getInstance("SHA1PRNG");
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < a.length; i++) {
				final int r = (int) (srgen.nextDouble() * (i + 1));
				final Object swap = a[r];
				a[r] = a[i];
				a[i] = swap;
			}
		}

		public Object[] result() {
			return items;
		}

		@SuppressWarnings("unchecked")
		public void choose(final int m, final int n) {
			if (limit != 0 && results.size() >= limit) return;
			if (n == 0) {
				// (apply function here)
				results.add(new ArrayList(Arrays.asList(items)));
				return;
			}
			if (m < n) return;
			items[n - 1] = data[m - 1];
			choose(m - 1, n - 1); // with item in position n
			choose(m - 1, n); // without item in position n (overwrite)
			return;
		}

	}

	public class PermComb<T> implements CGen<T> {
		Comb<T> comb;
		Perm<T> perm;

		PermComb(final Collection<T> itemList) {
			comb = new Comb<T>(itemList);
			perm = new Perm<T>(comb.next());
		}

		public boolean hasNext() {
			return (perm.hasNext() || comb.hasNext());
		}

		public Collection<T> next(final Collection<T> resultVector) {
			if (perm.hasNext()) return perm.next(resultVector);
			if (comb.hasNext()) {
				perm = new Perm<T>(comb.next());
			}
			if (perm.hasNext()) return perm.next(resultVector);
			return null;
		}

		public Collection<T> next() {
			if (perm.hasNext()) return perm.next();
			if (comb.hasNext()) {
				perm = new Perm<T>(comb.next());
			}
			if (perm.hasNext()) return perm.next();
			return null;
		}

		public String nextAsString(final Collection<T> rVector) {
			final Collection<T> resultVector = next(rVector);
			final StringBuffer sb = new StringBuffer();
			for (final T t : resultVector) {
				sb.append(t.toString() + " ");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		public String nextAsString() {
			return nextAsString(new ArrayList<T>());
		}

		public long count() {
			return perm.count() * comb.count(); // approx.
		}

		public Long maxCombID() {
			throw new UnsupportedOperationException();
		}

		public Collection<T> next(final Collection<T> resultVector, final T pad) {
			throw new UnsupportedOperationException();
		}

		public Collection<T> next(final T pad) {
			throw new UnsupportedOperationException();
		}

		public String nextAsString(final Collection<T> resultVector, final T pad) {
			throw new UnsupportedOperationException();
		}

		public String nextAsString(final T pad) {
			throw new UnsupportedOperationException();
		}

		public Long nextCombID() {
			throw new UnsupportedOperationException();
		}
	}

	public Collection<String> areWords(final Collection<String> input) {
		final List<String> areWords = new ArrayList<String>();
		final PermComb<String> pcString = new PermComb<String>(input);
		while (pcString.hasNext()) {
			final String wrd = pcString.nextAsString().replaceAll(" ", "");
			if (vocab.contains(wrd)) {
				areWords.add(wrd);
			}
		}
		return areWords;
	}

}
