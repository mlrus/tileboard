/*
 * Tester.cpp
 *
 *  Created on: Aug 16, 2009
 *      Author: mlrus
 */

#include <math.h>

#include "WordTester.h"

bool WordTester::matchCell(const char* word, Cell *cell, AXIS::Axis axis, L2Map& results) {
    vector<char>charSignature;
    uint incr = AXIS::getIncr(axis);
    bool hasIntersect = false;
    bool hasEmptyTile = false;
    char *charPtr = const_cast<char *> (word);
    WordTestResult wtr(cell, axis, word);
    Cell *cellPtr = cell;
    memcpy(useCount,ccount,Common::numCharValues);
    while (*charPtr) {
        if (cellPtr->isEmpty()) {
            if(useCount[*charPtr-'A']--<=0)return false;
            if (letterProbs.at(*charPtr-'A') == 0.0)
                return false;
            needsRackChar(&wtr, *charPtr);
            hasEmptyTile = true;
        } else if (*charPtr == cellPtr->getLetter()) {
            hasIntersect = true;
        } else
            return false;
        charSignature.push_back(*charPtr);
        charPtr++;
        if (cellPtr->isEndpoint(incr) && *charPtr)
            return false;
        cellPtr+=incr;
    }
    if (cellPtr->isFilled())
        return false;

    if (hasIntersect && hasEmptyTile) {
        Cell cellBuffer[Common::boardSize + 1];
        scrabble->board.cementWord(word, axis, cell, cellBuffer);
        bool res = checkNewcellPerpendiculars(cellBuffer, cell, axis);
        if(res) {
            // OK, if all those things are OK then we will use the score
            wtr.score = scrabble->board.scoreMove(cell,axis);
            wtr.wlogit = -1.0/log(wtr.prob) * wtr.score; // * wtr.score;
            results.addItem(charSignature,new WordTestResult(wtr));
        }
        scrabble->board.restoreWord(axis, cell, cellBuffer);
        return res;
    }
    return false;
}

bool WordTester::checkNewcellPerpendiculars(Cell* cellBuffer, Cell* cell, AXIS::Axis axis) {
    Cell* cellBufferPtr=cellBuffer;
    Cell* cellPtr = cell;
    AXIS::Axis dual = AXIS::getDual(axis);
    int incr = AXIS::getIncr(axis);
    if(!cell->isEndpoint(-incr) && cell->nextCell(-incr)->isFilled()) {
        return false;
    }

    while(cellBufferPtr->getLetter()) {
        // The cellBuffPtr is a copy of what the cell contained before this iteration.
        // The cellPtr is never empty or unfilled, but the cellBuffPtr is unfilled when
        // the item is getting filled on this iteration.  In this case, check if the new
        // value is compatible with any constraints.

        if(cellBufferPtr->isEmpty() && !scrabble->checkConstraints(cellPtr,dual)) {
            return false;
        }
        cellBufferPtr++;
        cellPtr+=incr;
    }

    // Check the predecessor to where we point (which is the one after the last word cell)
    if(!cellPtr->nextCell(-incr)->isEndpoint(incr)&&cellPtr->isFilled()) {
        cerr << "CAUGHT SUFFIX FILLED!" << endl;
        return false;
    }

    return true;
}

void WordTester::matchRow(const char * word, L2Map& results) {
    int wordSize = strlen(word);
    for (Cell *rowHead = scrabble->board.firstSquare(); rowHead < scrabble->board.lastSquare(); rowHead += Common::boardSize) {
        matchCell(word, rowHead,AXIS::HORIZONTAL, results);
        for (Cell *cell = rowHead; cell <= rowHead + Common::boardSize - wordSize - 1; cell++) {
            if (cell->isEmpty())
                matchCell(word,cell+1,AXIS::HORIZONTAL,results);
        }
    }
}

void WordTester::matchCol(const char * word, L2Map& results) {
    int wordSize = strlen(word);
    for (Cell *colHead = scrabble->board.firstSquare(); colHead < scrabble->board.firstSquare() + Common::boardSize; colHead++) {
        matchCell(word, colHead, AXIS::VERTICAL,results);
        for (Cell *cell = colHead; cell <= colHead + ( (Common::boardSize - wordSize - 1 ) *  Common::boardSize); cell
        += Common::boardSize) {
            matchCell(word,cell+1,AXIS::VERTICAL,results);
        }
    }
}

void WordTester::evaluatePotentialMatches(L2Map& results) {
    for (Dictionary::dictionaryHash xit = scrabble->dictionary->words.begin(); xit != scrabble->dictionary->words.end(); xit++) {
        matchRow(*xit, results);
        matchCol(*xit, results);
    }
}

void WordTester::showPotentialMatches(L2Map& results, uint numResults) {
    results.showItems(scrabble->board);
}

void WordTester::L2Map::addItem(vector<char> keyVec, WordTester::WordTestResult* wtr) {
    sort(keyVec.begin(),keyVec.end());
    string key;
    for(vector<char>::iterator it = keyVec.begin(); it!=keyVec.end(); it++) {
        key.push_back(*it);
    }
    WordTester::L2Map::L2MAP::iterator it = l2map.find(key);
    vector< WordTester::WordTestResult* > *v;
    if(it==l2map.end()) {
        v = new vector< WordTester::WordTestResult* >();
        v->push_back(wtr);
        l2map[*(new string(key))] = *v;
    } else {
        it->second.push_back(wtr);
    }
}

void WordTester::L2Map::pickfromTopPct() {
    topScore = INT_MIN;
    int poscnt = Common::considerTopPercentile*resequenced.size();
    for(vector< RVECLMT* >::iterator resIt = resequenced.end()-Common::considerTopPercentile*resequenced.size(); resIt!=resequenced.end(); resIt++) {
        RVECLMT* l  = *resIt;
        vector< WordTester::WordTestResult*> rva = l->first;
        if(rva[0]->score>topScore) {
            position = poscnt;
            topChars = l->second;
            topCell  = rva[0]->cell;
            topAxis  = rva[0]->axis;
            topWord  = rva[0]->word;
            topScore = rva[0]->score;
            topProb  = rva[0]->prob;
        }
        poscnt--;
    }
}

void WordTester::L2Map::resequenceItems() {
    resequenced.clear();
    for(L2MAP::iterator it = l2map.begin(); it!=l2map.end(); it++) {
        sort(it->second.begin(), it->second.end(), classcomp2());
        resequenced.push_back(new RVECLMT(it->second, it->first));
    }
    sort(resequenced.begin(), resequenced.end(), paircomp());
}

void WordTester::L2Map::emit(const Board& board, size_t numitems) {
    if(numitems>resequenced.size())numitems=resequenced.size();
    for(vector<RVECLMT*>::iterator iter = resequenced.end()-numitems; iter!=resequenced.end(); iter++) {
        RVECLMT* l = *iter;
        string key = l->second;
        vector< WordTester::WordTestResult* > vecl = l->first;
        cout << setw(10) << key << ": " << setw(12) <<  setprecision(6) << (*(vecl.begin()))->prob;
        emitChain(board,vecl);
        cout << endl;
    }
}

void WordTester::L2Map::showItems(const Board& board) {
    for(L2MAP::iterator it = l2map.begin(); it!=l2map.end(); it++) {
        string key = it->first;
        vector< WordTester::WordTestResult* >vec = it->second;
        cout << setw(7) << key << ": " ;
        emitChain(board,vec);
        cout << endl;
    }
}

void WordTester::L2Map::emitChain(const Board& board,  vector<WordTestResult *>& v) {
    for(vector<WordTestResult *>::iterator it2 = v.begin(); it2!=v.end(); it2++) {
        WordTester::WordTestResult* wtp = *it2;
        cout << " ["
        << setw(6) << wtp->word
        << " @" << board.describeCell(wtp->cell,wtp->axis)
        << " " << setw(2) << wtp->score
        << " " << setw(7) << setprecision(3) << wtp->wlogit << "]";
    }
}

void WordTester::L2Map::showItemsResequenced(const Board& board, uint numitems) {
    resequenceItems();
    pickfromTopPct();
    emit(board,numitems);
    resequenced.clear();
}
