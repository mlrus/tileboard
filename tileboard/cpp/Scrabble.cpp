/*
 * Scrabble.cpp
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#include <string>
#include <algorithm>
#include <ext/algorithm>
#include <iterator>
#include <climits>
#include <iomanip>
#include <stdlib.h>
#include <ostream>
#include <memory>

#include "Axis.h"
#include "Common.h"
#include "Scrabble.h"

string Scrabble::emptyString = "";

const char* Scrabble::getWordbuffer() {
    return wordBuffer;
}

const bool Scrabble::checkConstraints(const Cell* cell, AXIS::Axis axis) {
    Cell *from, *to;
    if (cell->isFilled() && board.getEndpoints(cell, axis, from, to)) {
        return checkConstraints(from, to, axis);
    }
    return false;
}

const inline bool Scrabble::checkConstraints(const Cell *from, const Cell *to, AXIS::Axis axis) {
    bool res = false;
    char vbuff[Common::boardSize + 1], *vptr = vbuff;
    if (from == to)
        res = true;
    else {
        uint incr = board.getIncrement(axis);
        for (const Cell *cell = from; cell <= to; cell += incr) {
            *vptr++ = cell->getLetter();//letter;
        }
        *vptr = 0;
        res = dictionary->contains(vbuff);
    }
    return res;
}

int getBestScore() {
    int bestScore = 0;
    return bestScore;
}

const int Scrabble::findmoveRecursive(Cell *cell, Cell* head, Cell *tail, const char *rack, AXIS::Axis axis, int *dirs) {

    //On entry, head should be the non-blank most before the cell, and tail the non-blank most after the cell
    if (*dirs==0 || rack == NULL || *rack == 0 || depth>=Common::rackSize-1)
        return 0;

    AXIS::Axis dual = AXIS::getDual(axis);
    if(*(rack+1)==0) {
        cell->set(*rack);
        int res = checkConstraints(cell,dual)?testBoardQuick(head,tail,cell,axis):0;
        cell->clear();
        return res;
    }

    depth++;
    int res = 0;
    char nextRack[Common::maxRackBreadth];

    //After tile is placed into nextHeadCell there can be a longer run to nextHeadEndpoint
    Cell *nextHeadCell, *nextHeadEndpoint=NULL, *nextTailCell, *nextTailEndpoint=NULL;
    board.extendEndpoints(cell, axis, head, tail, nextHeadCell, nextTailCell); //get the first blank in each direction
    int incval = axis==AXIS::HORIZONTAL?1:Common::boardSize;
    if(*dirs<0 && nextHeadCell!=NULL) {
        nextHeadEndpoint = nextHeadCell;
        while(!nextHeadEndpoint->isEndpoint(-incval)&&(nextHeadEndpoint-incval)->isFilled()) nextHeadEndpoint-=incval;
    }
    if(nextTailCell!=NULL) {
        nextTailEndpoint = nextTailCell;
        while(!nextTailEndpoint->isEndpoint(incval)&&(nextTailEndpoint+incval)->isFilled()) nextTailEndpoint+=incval;
    }

    // iterate over each character in the rack
    for (const char *ch = rack; *ch != 0; ch++) {
        cell->set(*ch);

        if (checkConstraints(cell, dual)) {
            testBoardQuick(head, tail, cell, axis); // assertion: no tiles  before head or after tail
            char *nextRackPtr = nextRack;
            for (const char *p = rack; *p != 0; p++) {
                if (p != ch)
                    *nextRackPtr++ = *p;
            }
            *nextRackPtr = 0;

            if(*dirs<0&&nextHeadCell!=NULL) {
                res = findmoveRecursive(nextHeadCell, nextHeadEndpoint, tail, nextRack, axis, dirs);
                if(nextTailCell!=NULL)
                    res = findmoveRecursive(nextTailCell, head, nextTailEndpoint, nextRack, axis, dirs+1);
            } else
                if (*dirs>0 &&
                        nextTailCell != NULL) {
                    res = findmoveRecursive(nextTailCell, head, nextTailEndpoint, nextRack, axis, dirs);
                }
        }
        cell->clear();
    }
    depth--;
    return res;
}

/***
 * Try each character in each position, and then try front/back and top/down if it works
 */

int Scrabble::evalBoardCurrent() {
    AXIS::Axis axes[] = { AXIS::HORIZONTAL, AXIS::VERTICAL };
    int dirvals[][3] = { { -1, 1, 0} , { -15, 15, 0 } };
    for (Cell *cell = board.firstSquare(); cell < board.lastSquare(); cell++) {
        if (cell->isEmpty() && board.hasAdjacent(cell)) {
            for (int i = 0; i < 2; i++) {
                AXIS::Axis axis = axes[i];
                int *dirs = dirvals[i];
                Cell *head, *tail;
                board.getEndpoints(cell, axis, head, tail);
                findmoveRecursive(cell, head, tail, rack.c_str(), axis, dirs);
            }
        }
    }

    if(resultList.size()>0) {
        bestScore = resultList.rbegin()->getScore();
        bestWord  = resultList.rbegin()->getWord();
        bestRow   = resultList.rbegin()->getRow();
        bestCol   = resultList.rbegin()->getCol();
        bestAxis  = resultList.rbegin()->getDirection();
    }
    else cout << "No result."<<endl;
//    if(resultList.size()>0)return resultList.rbegin()->getScore();
//    return 0;
    return resultList.size()>0?resultList.rbegin()->getScore():0;
}

void Scrabble::initCellChars() {
    for (Cell *cell = board.firstSquare(); cell < board.lastSquare(); cell++) {
        initCellChars(cell, AXIS::HORIZONTAL);
        initCellChars(cell, AXIS::VERTICAL);
    }
}

/***
 Precompute the valid adjacent characters along the perpendiculars
 */
void Scrabble::initCellChars(Cell *cell, AXIS::Axis axis) {
    Cell *from, *to;
    char vbuff[Common::boardSize + 1];
    char *vptr = vbuff;
    uint incr = board.getIncrement(axis);
    uint32_t mask = Common::ANYLETTER;
    board.getEndpoints(cell, axis, from, to);
    if (from != to) {
        mask = 0;
        for (const Cell *c = from; c <= to; c += incr) {
            *vptr++ = c->getLetter();//letter;
        }
        *vptr = 0;
        char * off = vbuff + (cell - from) / incr;
        for (unsigned int i = 0; i < rack.size(); i++) {
            uchar ch = rack[i];
            *off = ch;
            if (dictionary->contains(vbuff)) {
                mask |= 0x01 << (ch - 'A');
            }
        }
    }
    cell->setValidChars(mask, axis);
}

void Scrabble::showResults() {
    ostringstream str;
    cout << "Showing results " << resultList.size() << endl;
    uint numOut = 0;
    Cell cellbuff[Common::boardSize+1];
    for (set<Result, resultComp>::iterator it = resultList.begin(); it != resultList.end(); it++) {
        cout << it->toString() << endl;
        if(numOut+5>=resultList.size()) {
            board.cementWord(it->getWord().c_str(), it->getDirection(), board.getCell(it->getRow(), it->getCol()), cellbuff);
            str << it->toString() << endl;
            str << board.formatBoard() << '\n' << endl;
            board.restoreWord(it->getDirection(),board.getCell(it->getRow(),it->getCol()),cellbuff);
        }
        numOut++;
    }
    cout << "\n" << str.str();
    cout << "Emitted " << numOut << " items." << endl;
}
