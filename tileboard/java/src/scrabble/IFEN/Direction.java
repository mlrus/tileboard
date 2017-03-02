package scrabble.IFEN;


public enum Direction {

	LEFT {
		@Override
		int[] getStepValues() {
			return steps[0];
		}

		@Override
		public Axis getAxis() {
			return Axis.HORIZONTAL;
		}

	},
	RIGHT {
		@Override
		int[] getStepValues() {
			return steps[1];
		}

		@Override
		public Axis getAxis() {
			return Axis.HORIZONTAL;
		}

	},
	UP {
		@Override
		int[] getStepValues() {
			return steps[2];
		}

		@Override
		public Axis getAxis() {
			return Axis.VERTICAL;
		}

	},
	DOWN {
		@Override
		int[] getStepValues() {
			return steps[3];
		}

		@Override
		public Axis getAxis() {
			return Axis.VERTICAL;
		}

	},
	NONE {
		@Override
		public Axis getAxis() {
			return Axis.UNDEFINED;
		}

		@Override
		int[] getStepValues() {
			return steps[4];
		}
	};

	final static int[][] steps = new int[][] {
	// steps[0] moves one column to the left
			new int[] { 0, -1 },
			// steps[1] moves one column to the right.
			new int[] { 0, 1 },
			// steps[2] moves one column up.
			new int[] { -1, 0 },
			// steps[3] moves one column down
			new int[] { 1, 0 },
			// steps[4] does not move at all
			new int[] { 0, 0 } };

	abstract int[] getStepValues();

	public int getAdjacentRow() {
		return getStepValues()[0];
	}

	// Move by this amount to go horizontal
	public int getAdjacentColumn() {
		return getStepValues()[1];
	}

	abstract public Axis getAxis();

}
