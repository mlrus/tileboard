/**
 * 
 */
package scrabble.IFEN;

import java.util.EnumSet;

import scrabble.util.Cell;
import scrabble.util.Line;

public enum Axis {

	HORIZONTAL {

		@Override
		public Line getLine(final Cell cell) {
			return Line.getMaxRow(cell);
		}

		@Override
		public Line getDual(final Cell cell) {
			return VERTICAL.getLine(cell);
		}

		@Override
		public Direction getHeadDirection() {
			return Direction.LEFT;
		}

		@Override
		public Direction getTailDirection() {
			return Direction.RIGHT;
		}

		@Override
		public Direction[] getExtensionDirections() {
			return HorizontalDirections;
		}

	},
	VERTICAL {

		@Override
		public Line getLine(final Cell cell) {
			return Line.getMaxCol(cell);
		}

		@Override
		public Line getDual(final Cell cell) {
			return HORIZONTAL.getLine(cell);
		}

		@Override
		public Direction getHeadDirection() {
			return Direction.UP;
		}

		@Override
		public Direction getTailDirection() {
			return Direction.DOWN;
		}

		@Override
		public Direction[] getExtensionDirections() {
			return VerticalDirections;
		}

	},
	UNDEFINED {

		@Override
		public Line getLine(final Cell cell) {
			return null;
		}

		@Override
		public Line getDual(final Cell cell) {
			return null;
		}

		@Override
		public Direction getHeadDirection() {
			return Direction.NONE;
		}

		@Override
		public Direction getTailDirection() {
			return Direction.NONE;
		}

		@Override
		public Direction[] getExtensionDirections() {
			// TODO Auto-generated method stub
			return null;
		}

	};
	public abstract Line getLine(Cell cell);

	public abstract Line getDual(Cell cell);

	public abstract Direction getHeadDirection();

	public abstract Direction getTailDirection();

	public Cell extendHead(final Line line) {
		return line.expandLine(getHeadDirection());
	}

	public Cell extendTail(final Line line) {
		return line.expandLine(getTailDirection());
	}

	public abstract Direction[] getExtensionDirections();

	final static Direction[] HorizontalDirections = new Direction[] { Direction.LEFT, Direction.RIGHT };

	final static Direction[] VerticalDirections = new Direction[] { Direction.UP, Direction.DOWN };

	public static final EnumSet<Axis> HVAxis = EnumSet.of(Axis.HORIZONTAL, Axis.VERTICAL);

}
