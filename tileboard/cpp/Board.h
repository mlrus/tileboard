/*
 * Board.h
 *
 *  Created on: Jun 26, 2009
 *      Author: mlrus
 */

#ifndef BOARD_H_
#define BOARD_H_

#include <iostream>
#include <fstream>
#include <string>
#include <set>
#include <vector>
#include <cstddef>

#include "Common.h"
#include "Storable.h"
#include "Cell.h"
#include "Axis.h"
#include "Util.h"

class Board: public Storable {

    public:

        enum SquareType {
            RG = 0x11, DL = 0x12, TL = 0x13, DW = 0x21, TW = 0x31
        };

    private:
        static const SquareType standardScrabbleTiles[Common::boardSize * Common::boardSize];
        Cell *squares;
        Cell *priorSquares;
        void preservePriorsquares() {
            memcpy(priorSquares, squares, Common::boardSize * Common::boardSize * sizeof(Cell));
        }

    public:

        typedef const Cell * (Board::* const getNeighbor)(const Cell *);
        static const getNeighbor neighborAccess[4];
        static const getNeighbor neighborHorizontalAccess[2];
        static const getNeighbor neighborVerticalAccess[2];
        static const int numNeighbors;

        static const SquareType* const standardTilesStart();
        static const SquareType* const standardTilesEnd();

        const bool changed(const Cell* cell);
        const string formatBoard(const string& prefix=Common::EMPTYSTRING, bool showIfCovered=false) const;
        const string getBoardString();
        const string toString();
        const string& cleanString(string& string, const string& valid, uchar replacement);
        inline Cell* getCell(uint row, uint col) { return squares + row * Common::boardSize + col; }
        inline const bool validCell(const Cell *cell) const { return cell >= squares && cell < squares + Common::boardSize * Common::boardSize; }
        inline const int col(const Cell *cell) const { return (cell - squares) % Common::boardSize; }
        inline void setCell(Cell* c, uchar letter) { c->set(letter); }
        inline void setCell(const Cell& c, uchar letter) { (const_cast<Cell *> (&c))->set(letter); }
        string describeCell(Cell *cell, AXIS::Axis axis = AXIS::HORIZONTAL) const;
        string describeCell(const Cell *cell, AXIS::Axis axis = AXIS::HORIZONTAL) const;
        uint scoreAxis(const Cell* cell, AXIS::Axis axis, string prefix = "");
        uint scoreMove(const Cell* cell, AXIS::Axis axis);
        inline void cementTile(Cell& cell) {  cell.assertCovered(); }
        void addWord(const string& word, const AXIS::Axis axis, const int row, const int col);
        void cementWord(const char *word, const AXIS::Axis axis, Cell *cell, Cell* copyBuff);
        void restoreWord(const AXIS::Axis axis, Cell *cell, Cell* copyBuff);

        void initCell(Cell* c, uchar initMask, bool rowZero, bool rowN, bool colZero, bool colN);
        void read(const string& filename);
        void reset();
        void retractCell(Cell& c);

        inline bool getEndpoints(const Cell* cell, AXIS::Axis axis, Cell*& from, Cell*& to) {
            from = const_cast<Cell *> (cell);
            to = const_cast<Cell *> (cell);
            return getEndpoints(axis, from, to);
        }

        inline bool getEndpoints(AXIS::Axis axis, Cell*& from, Cell*& to) {
            int incr=1;
            switch(axis) {
                case AXIS::HORIZONTAL:
                    while(!from->colZero && (from-1)->isFilled()) {
                        from--;
                    }
                    while(!to->colN && (to+1)->isFilled()) {
                        to++;
                    }
                    break;
                case AXIS::VERTICAL:
                    incr = getIncrement(axis);
                    while(!from->rowZero && (from-incr)->isFilled()) {
                        from-=incr;
                    }
                    while(!to->rowN && (to+incr)->isFilled()) {
                        to+=incr;
                    }
                    break;
                default:
                    cerr << "Error: invalid direction." << endl;
                    return false;
            }
            return true;
        }

        void extendEndpoints(Cell*& about, AXIS::Axis axis, Cell*& from, Cell*& to, Cell*& head, Cell*& tail) {
            if(about->letter!=Common::EMPTYCHAR) {
                extendEndpoints(axis,from,to,head,tail);
                return;
            }
            uchar cellLetter = about->letter;
            about->letter = 'X';
            extendEndpoints(axis,from,to,head,tail);
            about->letter=cellLetter;
        }

        void extendEndpoints(AXIS::Axis axis, Cell*& from, Cell*& to, Cell*& head, Cell*& tail) {
            uint incr = AXIS::getIncr(axis); // axis==AXIS::HORIZONTAL?1:Common::boardSize;
            head = from;
            tail = to;
            while(head->isFilled()&&!head->isEndpoint(-incr))
                head-=incr;
            if(head->isFilled())head=NULL;
            while(tail->isFilled()&&!tail->isEndpoint(incr))
                tail+=incr;
            if(tail->isFilled())tail=NULL;
        }

        inline const int row(const Cell *cell) const {
            Cell * sq = squares;
            ptrdiff_t diff = cell - sq;
            return diff / Common::boardSize;
        }

        inline const Cell* getLeft(const Cell *cell) {
            if (col(cell) > 0)
                return cell - 1;
            return NULL;
        }

        inline const Cell* getRight(const Cell * cell) {
            if (col(cell) < Common::boardSize - 1)
                return cell + 1;
            return NULL;
        }

        inline const Cell* getUp(const Cell * cell) {
            if (row(cell) > 0)
                return cell - Common::boardSize;
            return NULL;
        }

        inline const Cell* getDown(const Cell * cell) {
            if (row(cell) < Common::boardSize - 1)
                return cell + Common::boardSize;
            return NULL;
        }

        inline const AXIS::Axis getAxis(const Cell* head, const Cell* tail);

        inline const uint getIncrement(AXIS::Axis axis) const {
            return (axis == AXIS::VERTICAL) ? Common::boardSize : 1;
        }

        inline bool validChar(uchar letter) {
            return letter != Common::EMPTYCHAR;
        }

        inline bool hasAdjacent(const Cell *cell, AXIS::Axis axis) {
            if (cell >= squares && cell < squares + Common::boardSize * Common::boardSize) {
                Board::getNeighbor *nlist = axis == AXIS::HORIZONTAL ? neighborHorizontalAccess : neighborVerticalAccess;
                const Cell *neighbor;
                for (int i = 0; i < 2; i++) {
                    neighbor = (this->*nlist[i])(cell);
                    if (neighbor != NULL && neighbor->isFilled())
                        return true;
                }
            }
            return false;
        }

        inline bool hasAdjacent(const Cell *cell) {
            if (cell >= squares && cell < squares + Common::boardSize * Common::boardSize) {
                const Cell *neighbor;
                for (int i = 0; i < numNeighbors; i++) {
                    neighbor = (this->*neighborAccess[i])(cell);
                    if (neighbor != NULL && neighbor->isFilled())
                        return true;
                }
            }
            return false;
        }

        inline Cell *firstSquare() const {
            return squares;
        }

        inline Cell *lastSquare() const {
            return firstSquare() + Common::boardSize * Common::boardSize;
        }

        Board& operator=(const Board& b) {
            if(this!=&b) {
                memcpy(squares, b.squares, Common::boardSize * Common::boardSize * sizeof(Cell));
                memcpy(priorSquares, b.priorSquares, Common::boardSize * Common::boardSize * sizeof(Cell));
            }
            return *this;
        }

        Board(const Board &b) :
            Storable(),
            squares(new Cell[Common::boardSize * Common::boardSize]),
            priorSquares(new Cell[Common::boardSize * Common::boardSize]) {
            memcpy(squares, b.squares, Common::boardSize * Common::boardSize * sizeof(Cell));
            memcpy(priorSquares, b.priorSquares, Common::boardSize * Common::boardSize * sizeof(Cell));
        }

        Board() :
            Storable(), squares(new Cell[Common::boardSize * Common::boardSize]), priorSquares(
                    new Cell[Common::boardSize * Common::boardSize]) {
        }

        Board(string& filename) :
            Storable(), squares(new Cell[Common::boardSize * Common::boardSize]), priorSquares(
                    new Cell[Common::boardSize * Common::boardSize]) {
            Cell *cptr = squares;
            for (const SquareType *tile = standardTilesStart(); tile < standardTilesEnd(); tile++) {
                SquareType sq = *tile;
                initCell(cptr, sq, row(cptr)==0, row(cptr)==Common::boardSize-1,
                        col(cptr)==0, col(cptr)==Common::boardSize-1);
                cptr++;
            }
            read(filename);
            preservePriorsquares();
        }

        virtual ~Board() {
             delete[] squares;
             delete[] priorSquares;
        }
};

#endif /* BOARD_H_ */
