package test;

import scrabble.util.Board;

public class ReadManyBoardsAtOnce {

	class DoBoard implements Runnable {
		String name;
		Board board;
		int id;

		DoBoard(final String name, final int id) {
			this.name = name;
			this.id = id;
		}

		Board getBoard() {
			return board;
		}

		@Override
		public void run() {
			System.out.println(">>" + id);
			board = new Board(name);
			System.out.println("<<" + id);

		}
	}

	public static void main(final String[] args) throws InterruptedException {
		final ReadManyBoardsAtOnce rb = new ReadManyBoardsAtOnce();
		final DoBoard[] boarders = new DoBoard[100];
		final Thread[] threads = new Thread[100];
		int bcount = 0;
		do {
			for (final String arg : args) {
				boarders[bcount] = rb.new DoBoard(arg, bcount);
				threads[bcount] = new Thread(boarders[bcount]);
				bcount++;
				if (bcount >= 100) {
					break;
				}
			}
		} while (bcount < 100);
		for (final Thread th : threads) {
			th.start();
		}

		Thread.sleep(1000);
		while (true) {
			boolean live = false;
			for (int i = 0; i < bcount; i++) {
				if (threads[i].isAlive()) {
					live = true;
					Thread.sleep(100);
					break;
				}
			}
			if (!live) {
				break;
			}
		}

		for (int i = 0; i < bcount; i += 2) {
			System.out.println(boarders[i].getBoard().toString());
		}
		for (int i = 1; i < bcount; i += 2) {
			System.out.println(boarders[i].getBoard().toString());
		}
	}
}
