/*
 * Scrabble.h
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#ifndef SCRABBLE_H_
#define SCRABBLE_H_

#include <vector>
#include <iterator>
#include <set>
#include <functional>

#include <sstream>
#include <iostream>
#include <ostream>
#include <iomanip>
#include <memory>

#include "Common.h"
#include "Axis.h"
#include "Cell.h"
#include "Storable.h"
#include "Dictionary.h"
#include "Util.h"
#include "Board.h"
#include "Result.h"

using namespace std;

class Scrabble {
    friend class GameManager;
    friend class WordTester;

    static const bool debug = false;
    void operator=(const Scrabble&);
    Scrabble(const Scrabble&);

    char wordBuffer[Common::boardSize + 1];
    char validationBuffer[Common::boardSize + 1];
    static string emptyString;

    static inline bool resultLess(const Result &a, const Result &b) {
        if (a.score != b.score)
            return a.score < b.score;
        int cmp = a.word.compare(b.word);
        if (cmp != 0)
            return cmp < 0;
        if (a.row != b.row)
            return a.row < b.row;
        if (a.col != b.col)
            return a.col < b.col;
        if (a.direction != b.direction)
            return a.direction < b.direction;
        return false;
    }

    struct resultComp {
        bool operator()(const Result& a, const Result& b) {
            return resultLess(a, b);
        }
        ~resultComp() { }
    };

    struct ResultLess: public std::binary_function<Result, Result, bool> {
        bool operator()(const Result &a, const Result&b) const {
            return resultLess(a, b);
        }
        virtual ~ResultLess() {
        }
    };


    Dictionary *dictionary;
    public:
        set<Result, resultComp> resultList;
        string bestWord;
        int bestScore,bestRow, bestCol;
        AXIS::Axis bestAxis;
        uint depth;

    public:
        Board board;
        string rack;
        Scrabble(Dictionary* dictionary, Board& board, string& rack = emptyString) :
            dictionary(dictionary),
            resultList(),
            bestWord(),
            bestScore(0),
            bestRow(-1),
            bestCol(-1),
            bestAxis(AXIS::INVALID),
            depth(0),
            board(board),
            rack(rack) {
        }

        Scrabble(const Scrabble* scr) :
            dictionary(scr->dictionary),
            resultList(),
            bestWord(),
            bestScore(0),
            bestRow(-1),
            bestCol(-1),
            bestAxis(AXIS::INVALID),
            depth(0),
            board(),
            rack(scr->rack) {
            board = scr->board;
        }

        virtual ~Scrabble() {
        }

        void addResult(string& word, uint score, uint row, uint col, AXIS::Axis direction) {
            Result *res = new Result(word, score, row, col, direction);
            resultList.insert(*res);
        }

        void addResult(const char *str, uint score, uint row, uint col, AXIS::Axis direction) {
            addResult(*(new string(str)), score, row, col, direction);
        }

        const inline bool checkWord(Cell *from,  Cell *to, AXIS::Axis axis)  {
            uint incr = board.getIncrement(axis);
            char *wptr = wordBuffer;
            for (const Cell *cell = from; cell <= to; cell = cell + incr) {
                *wptr++ = cell->getLetter();
            }
            *wptr = 0;
            return dictionary->contains(wordBuffer);
        }

        inline uint testBoardQuick(Cell *from, Cell *to,  Cell *expandLoc, AXIS::Axis axis) {
            uint val = 0;
            if (checkWord(from, to, axis)) {
                val = board.scoreMove(expandLoc, axis);
                string *word = new string(getWordbuffer());
                uint row = board.row(from);
                uint col = board.col(from);
                addResult(*word, val, row, col, axis);
            }
            return val;
        }

        int evalBoardCurrent();

        const int findmoveRecursive(Cell *acceptor, Cell* head, Cell *tail, const char *rack, AXIS::Axis axis, int *dirs);

        const inline bool checkConstraints(const Cell* from, const Cell *to, AXIS::Axis axis); // Check if perpendicular run is length=1 or a word
        const bool checkConstraints(const Cell* cell, AXIS::Axis axis); // Check if perpendicular run is length=1 or a word

        const uint scoreRange(const Cell* from, const Cell* to); // Score the range (assumes from and to are set correctly, and content is a word)

        void setRack(string& rackStr) { rack = rackStr; }
        void initCellChars();
        void initCellChars(Cell *cell, AXIS::Axis axis);

        const char* getWordbuffer();
        void showResults();
        void showValidChars();
        const uint dictionarySize() { return dictionary->words.size(); }
};

#endif /* SCRABBLE_H_ */
