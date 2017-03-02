# tileboard

GWT and C++ with protobuf for automatic Scrabble

General search operators applied to solving scrabble board game. Optimization of the word choice can use keymatch applications, as well as combinatorial optimization and heuristics.

scrabble |ˈskrabəl| noun 1. sing. an act of scratching or scrambling for something : he heard the scrabble of claws behind him. 1. ( Scrabble) trademark a board game in which players use lettered tiles to create words in a crossword fashion.

Note: The trademarked board game should not be used for commercial purposes without appropriate permission of the Trademark holder

The program is invoked with a dictionary, a board and a rack of letters, like this:

```
./a.out -d dicts/TWL06.txt -b boards/move.002.board QEOBMTC
```

The board should consist of fifteen rows with fifteen columns per row, as in the first chunk on the left. The output chunk is shown as a convenience to the right, but should not be provided as input. The scored results are all that is normally emitted. The notation NOT N01 indicates the letter sequence NOT occurs in column N beginning at row one. The notation NOT 03L indicates the letter sequence NOT occurs on row three beginning in column L.

The Web GUI is written entirely in GWT (GWT 1.7.0). This allows multiple actions: 1. Enter data onto the board by clicking on a square and then simply type. The cursor will keep advancing to the next available square. There is an internal settable to enable/disable wraps (at end of column or row). 1. Select a best-move from the solution presented on the right side of the board. Mouse-over a word to see where it appears on the board, and click to select it 1. Solve the board (and rack) by sending them to the backend and receive back a list of candidate moves

The solver is fast and neat, and a human-computer-interface (HCI) allows you to easily examine, modify and utilize the results as shown in these screenshots.
