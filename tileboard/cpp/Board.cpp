/*
 * Board.cpp
 *
 *  Created on: Jun 26, 2009
 *      Author: mlrus
 */

#include <strings.h>
#include <sstream>
#include <iostream>
#include <ostream>
#include <iomanip>
#include <ctype.h>

#include "Common.h"
#include "Cell.h"
#include "Board.h"
#include "Util.h"

Board::SquareType const Board::standardScrabbleTiles[Common::boardSize * Common::boardSize] = {

        TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW,

        RG, DW, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, DW, RG,

        RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG,

        DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL,

        RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG,

        RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG,

        RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG,

        TW, RG, RG, DL, RG, RG, RG, DW, RG, RG, RG, DL, RG, RG, TW,

        RG, RG, DL, RG, RG, RG, DL, RG, DL, RG, RG, RG, DL, RG, RG,

        RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, TL, RG,

        RG, RG, RG, RG, DW, RG, RG, RG, RG, RG, DW, RG, RG, RG, RG,

        DL, RG, RG, DW, RG, RG, RG, DL, RG, RG, RG, DW, RG, RG, DL,

        RG, RG, DW, RG, RG, RG, DL, RG, DL, RG, RG, RG, DW, RG, RG,

        RG, DW, RG, RG, RG, TL, RG, RG, RG, TL, RG, RG, RG, DW, RG,

        TW, RG, RG, DL, RG, RG, RG, TW, RG, RG, RG, DL, RG, RG, TW

};

const Board::SquareType* const Board::standardTilesStart() {
    return standardScrabbleTiles;
}

const Board::SquareType* const Board::standardTilesEnd() {
    return standardScrabbleTiles + (Common::boardSize * Common::boardSize);
}

const int Board::numNeighbors = 4;
const Board::getNeighbor Board::neighborAccess[Board::numNeighbors] = { &Board::getLeft, &Board::getRight,
        &Board::getUp, &Board::getDown };
const Board::getNeighbor Board::neighborHorizontalAccess[2] = { &Board::getLeft, &Board::getRight };
const Board::getNeighbor Board::neighborVerticalAccess[2] = { &Board::getUp, &Board::getDown };

inline const AXIS::Axis Board::getAxis(const Cell* head, const Cell* tail) {
    if (head == tail)
        return AXIS::POINT;
    int headIndx = head - squares;
    int tailIndx = tail - squares;
    if ((tailIndx - headIndx) % Common::boardSize == 0)
        return AXIS::VERTICAL;
    return AXIS::HORIZONTAL;
    //    if (headIndx / Common::boardSize == tailIndx / Common::boardSize)
    //        return AXIS::HORIZONTAL;
    //    return AXIS::INVALID;
}

const string& Board::cleanString(string& src,
        const string& valid = Common::VALIDCHARS,
        uchar replacement = Common::EMPTYCHAR) {
    size_t pos = 0;
    while ((pos = src.find_first_not_of(valid, pos)) != string::npos) {
        src[pos] = replacement;
    }
    return src;
}

void Board::read(const string& filename) {
    try {
        ifstream infile(filename.c_str(), std::ios_base::in);
        string line;
        Cell *cellPtr = firstSquare();
        Cell *endptr = lastSquare();
        while (cellPtr < endptr && getline(infile, line, '\n')) {
            Util::trim(line);
            const char *cstr = line.c_str();
            Cell *eptr = cellPtr + Common::boardSize;
            while (cellPtr < eptr) {
                if( (cellPtr->set(*cstr))->isValid())cementTile(*cellPtr);
                cellPtr++;
                if (*cstr != 0)
                    cstr++;
            }
        }
        infile.close();
        cout << formatBoard();
    } catch (...) {
        cerr << "Error reading " << filename << endl;
    }
    preservePriorsquares();
}

const bool Board::changed(const Cell* cell) {
    ptrdiff_t dif = cell - squares;
    Cell *otherCell = priorSquares + dif;
    bool chg = (*cell != *otherCell);
    return chg;
}

void Board::addWord(const string& testWord, const AXIS::Axis testAxis, const int testRow, const int testCol) {
    Cell *cell = getCell(testRow, testCol);
    int incr = getIncrement(testAxis);
    for(uint i=0;i<testWord.size();i++) {
        cell->set(testWord[i]);
        cell+=incr;
    }
}

void Board::cementWord(const char *word, const AXIS::Axis testAxis, Cell *cell, Cell* copyBuff) {
    int incr = getIncrement(testAxis);
    char *testWord = const_cast<char *>(word);
    do {
        *copyBuff++ = *cell; //*testWord+=incr;
        cell->set(*testWord++);
        cell->assertCovered();
        cell+=incr;
    } while(*testWord);
    copyBuff->letter = (char)0;
}

void Board::restoreWord(const AXIS::Axis axis, Cell *cell, Cell* copyBuff) {
    int incr = getIncrement(axis);
    while(copyBuff->letter!=(char)0) {
        *cell = *copyBuff++;
        cell+=incr;
    }
}

uint Board::scoreMove(const Cell* cell, AXIS::Axis axis) {
    uint primaryScore = scoreAxis(cell, axis);
    uint dualScores = 0;
    uint numChanged = 0;
    Cell *from, *to;
    uint incr = getIncrement(axis);
    AXIS::Axis dual = getDual(axis);
    if (getEndpoints(cell, axis, from, to)) {
        for (const Cell* c = from; c <= to; c += incr) {
            if (changed(c)) {
                numChanged++;
                uint sec = scoreAxis(c, dual, "   ");
                dualScores += sec;
            }
        }
    }
    uint totalScore = primaryScore + dualScores;
    if(numChanged == Common::rackSize) totalScore+=Common::bingoValue;
    return totalScore;
}

uint Board::scoreAxis(const Cell* cell, AXIS::Axis axis, string prefix) {
    uint score = 0;
    uint wf = 1;
    uint incr = getIncrement(axis);
    Cell *from, *to;
    if (getEndpoints(cell, axis, from, to) && from != to) {
        //cout << "\n\n";
        for (const Cell* c = from; c <= to; c += incr) {
            uint lf = c->letterFactor;
            uint lwf = c->wordFactor;
            uint va = c->value;
            wf *= lwf;
            score += lf * va;
        }
        score *= wf;
    }
    return score;
}

void Board::initCell(Cell* c, uchar initMask, bool rowZero, bool rowN, bool colZero, bool colN) {
    c->wordFactor = (initMask >> 4);
    c->letterFactor = (initMask & 0x0F);
    c->rowZero = rowZero;
    c->rowN = rowN;
    c->colZero = colZero;
    c->colN = colN;
}

void Board::retractCell(Cell& c) {
    c.set(Common::EMPTYCHAR);
}

void Board::reset() {
    bzero(squares, sizeof(Cell) * Common::boardSize * Common::boardSize);
    Cell *cptr = squares;
    for (const SquareType *tile = standardTilesStart(); tile < standardTilesEnd(); tile++)
        setCell(*cptr++, *tile);
    preservePriorsquares();
}

const string Board::formatBoard(const string& prefix, const bool showIfCovered) const {
    ostringstream str;
    Cell *cellPtr = firstSquare();
    Cell *endptr = lastSquare();
    str << prefix << "  | ABCDEFGHIJKLMNO";
    int row = 1;

    while (cellPtr < endptr) {
        str << '\n' << prefix << setw(2) << setfill('0') << row++ << "| " << setw(1);
        Cell *eptr = cellPtr + Common::boardSize;
        while (cellPtr < eptr) {
            if (cellPtr->isValid())
                str << (char) (cellPtr->getLetter());
            else
                str << "_";
            if(showIfCovered)str<<(cellPtr->covered?'#':' ');
            cellPtr++;
        }
    }
    return str.str();
}

const string Board::toString() {
    string result;
    Cell *cellPtr = firstSquare();
    Cell *endptr = lastSquare();
    while (cellPtr < endptr) {
        Cell *eptr = cellPtr + Common::boardSize;
        while (cellPtr < eptr) {
            uchar letter = cellPtr->letter;
            if(cellPtr->value==0)letter=tolower(letter);
            result.push_back(letter == 0 ? '_' : letter);
            cellPtr++;
        }
        result.push_back('\n');
    }
    return result;
}

const string Board::getBoardString() {
    ostringstream str;
    Cell *cellPtr = firstSquare();
    Cell *endptr = lastSquare();
    while (cellPtr < endptr) {
        Cell *eptr = cellPtr + Common::boardSize;
        while (cellPtr < eptr) {
            str << cellPtr->toString() << '|';
            cellPtr++;
        }
        str << '\n';
    }
    return str.str();
}

string Board::describeCell(Cell *cell, AXIS::Axis axis) const {
    ostringstream str;
    if(cell==NULL)str<<"NULL";
    else {
        uchar ch = 'A' + col(cell);
        char rowlabel[3];
        snprintf(rowlabel,3,"%02d",row(cell)+1);
        switch(axis) {
            case AXIS::HORIZONTAL:
                str << rowlabel << ch;
                break;
            case AXIS::VERTICAL:
                str << ch << rowlabel;
                break;
            default:
                str << "("<<ch<<","<<rowlabel<<")";
        }
    }
    return str.str();
}

string Board::describeCell(const Cell *cell, AXIS::Axis axis) const {
    Cell * c = const_cast<Cell *> (cell);
    return describeCell(c, axis);
}
