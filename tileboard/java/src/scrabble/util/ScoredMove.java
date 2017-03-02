/**
 * 
 */
package scrabble.util;

import scrabble.ScrabbleEvaluator;

public class ScoredMove implements Comparable<ScoredMove> {
	/**
	 * 
	 */
	private final ScrabbleEvaluator constraintHierarchy;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOuterType().hashCode();
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + score;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ScoredMove other = (ScoredMove) obj;
		if (!getOuterType().equals(other.getOuterType())) return false;
		if (line == null) {
			if (other.line != null) return false;
		} else if (!line.equals(other.line)) return false;
		if (score != other.score) return false;
		if (word == null) {
			if (other.word != null) return false;
		} else if (!word.equals(other.word)) return false;
		return true;
	}

	private final int score;
	private final String word;
	private final Line line;

	public ScoredMove(final ScrabbleEvaluator constraintHierarchy, final int score, final String word, final Line line) {
		super();
		this.constraintHierarchy = constraintHierarchy;
		this.score = score;
		this.word = word;
		this.line = line;
	}

	@Override
	public String toString() {
		return String.format("%04d %-20s %s", score, word, line);
	}

	public final int getScore() {
		return score;
	}

	public final String getWord() {
		return word;
	}

	public final Line getLine() {
		return line;
	}

	@Override
	public int compareTo(final ScoredMove o) {
		int cmp = this.score - o.score;
		if (cmp == 0) {
			cmp = this.word.compareTo(o.word);
		}
		return cmp;
	}

	private ScrabbleEvaluator getOuterType() {
		return this.constraintHierarchy;
	}
}