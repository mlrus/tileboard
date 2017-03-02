/*
 * GameManager.cpp
 *
 *  Created on: Aug 13, 2009
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

#include "GameManager.h"
#include "WordTester.h"
#include "Clocker.h"


/***
 * The GameManager class combines a board and all the racks declared for the board.
 */
auto_ptr<string> GameManager::ident(int maxCount) {
    char buff[80];
    sprintf(buff, " %d/%d ( /%d) p%d     ", currLookahead, maxLookahead, maxCount, player);
    auto_ptr<string> res(new string(buff));
    for (int i = 0; i < currLookahead; i++)
        res->append("  ");
    return res;
}
auto_ptr<string> GameManager::ident(int nCount, int maxCount) {
    char buff[80];
    sprintf(buff, " %d/%d (%d/%d) p%d     ", currLookahead, maxLookahead, nCount, maxCount, player);
    auto_ptr<string> res(new string(buff));
    for (int i = 0; i < currLookahead; i++)
        res->append("  ");
    return res;
}
auto_ptr<string> GameManager::ident(int nCount, int maxCount, int rackNo) {
    char buff[80];
    sprintf(buff, " %d/%d (%d/%d) p%d %3d ", currLookahead, maxLookahead, nCount, maxCount, player, rackNo);
    auto_ptr<string> res(new string(buff));
    for (int i = 0; i < currLookahead; i++)
        res->append("  ");
    return res;
}

/***
 * Method evalAndCountermove considers the "average" game which can be played by use of any tile that is not
 * in the current racks.  This gives broad statistics on what the average move could be by the opponent.
 */
int GameManager::evalAndCountermove() {
    Cell cellBuffer[Common::boardSize + 1];
    string bestChosenWord, bestCountermove;
    int nCount = 0;

    this->evalBoardCurrent();
    this->showResults();

    WordTester::L2Map resultMap;
    string availableLetters;
    Clocker localClock;

    for (set<Result, resultComp>::reverse_iterator it = resultList.rbegin(); ++nCount <= numTopNToTry && it
    != resultList.rend(); it++) {
        localClock.begin();
        cout << "\n:: " << localClock.tod() << " :: Begin evaluatePotentialMatches" << endl;
        resultMap.clear();
        availableLetters.clear();
        rack.assign(gameRacks.at(player));
        string outerWord = it->getWord();
        int outerScore = it->getScore();
        AXIS::Axis outerDirection = it->getDirection();
        int outerRow = it->getRow();
        int outerCol = it->getCol();
        cementWordAndCleanRack(outerWord, outerDirection, outerRow, outerCol, cellBuffer);
        cout << "ASSUME OUTERWORD: " << outerWord
        <<" " << AXIS::mkLabel(outerDirection,outerRow,outerCol)
        << "; OUTERSCORE: " << outerScore << '\n'
        << board.formatBoard("    ") << endl;

string availableLetters;
        getAvailableLetters(availableLetters);
        WordTester tester((Scrabble *)this,availableLetters);

WordTester::L2Map resultMap;

        tester.evaluatePotentialMatches(resultMap);

        resultMap.showItemsResequenced(board,Common::numToDisplay);

        cout << "BEST RESPONSE TO \"" << outerWord
        <<"\" @"<<AXIS::mkLabel(outerDirection,outerRow,outerCol);
        cout << " score=" << outerScore;
        cout << " IS \"" << resultMap.getTopWord()
        << "\" @" << board.describeCell(resultMap.getTopCell(),resultMap.getTopAxis())
        << "  with " << resultMap.getTopScore()
        << " points (item #" << resultMap.getPos()
        << ") for letters " << resultMap.getTopChars()
        << " (top " << Common::considerTopPercentile << "%), Net change = "
        << (outerScore- resultMap.getTopScore()) << endl;
        board.restoreWord(it->getDirection(), board.getCell(outerRow, outerCol), cellBuffer);
        cout << localClock.readResetDetailed() << endl;
    }
    return 0;
}


/***
 * Method evalBoardsForward simulates game play with recursive scrabble against the next player, up to
 * a depth of maxLookahead tiers.  This cannot evaluate every possible "next board" so the number of
 * boards is set by the -nr  (number of racks) input parameter and the -nt (number to try) which sets
 * the number of best moves top pieces to consider.
 */
int GameManager::evalBoardsForward() {

    /***
     * Assign the current player's rack to the active rack,
     * and then evaluate the board with the active rack.
     * The "results" iterator will return the valid moves
     * in reverse order of score. The board does not change.
     */

    evalBoardCurrent();

    if (currLookahead >= maxLookahead) {
        return resultList.rbegin()->getScore();
    }

    currLookahead++;

    Cell cellBuffer[Common::boardSize + 1];
    string bestChosenWord, bestCountermove;
    int bestChosenScore = INT_MIN;
    //bool bestInitalized = false;

    /***
     * Save current rack under current player in the subGame,
     * advance to the next player, and update the rack to the
     * newly active player's rack.  The new and old players
     * will be the same if nP=true.
     */
    GameManager *subGame = new GameManager(this);

    int nCount = 0;
    //Evaluate forward, from largest score towards smaller scores.  This uses a reverse iterator.
    for (set<Result, resultComp>::reverse_iterator it = resultList.rbegin(); ++nCount <= numTopNToTry && it
    != resultList.rend(); it++) {
        string outerWord = it->getWord();
        int outerScore = it->getScore();
        AXIS::Axis outerDirection = it->getDirection();
        int outerRow = it->getRow();
        int outerCol = it->getCol();
        subGame->useCurrentPlayer();
        subGame->cementWordAndCleanRack(outerWord, outerDirection, outerRow, outerCol, cellBuffer);
        // The rack is now less than rackSize letters, since some letters were played onto the board
        subGame->setWorkdRack();

        cout << ">>" << *ident(numTopNToTry) << "evalAll(" << outerWord << "=" << setw(3) << setfill('0') << outerScore
        << ", numRacks=" << numRacksToTry << ", maxPool=" << rackMaxPool << ")" << endl;

        string availableLetters;
        getAvailableLetters(availableLetters);

        int strongestCounterScore = INT_MAX;
        string strongestCounterWord;
        // Repeatedly fill missing squares of the rack, and then recurse [note this could leverage shared prefix]
        for (int tries = 0; tries < numRacksToTry; tries++) {
            subGame->resultList.clear();
            subGame->useWorkRack();
            subGame->extendRack(Common::rackSize, availableLetters); // extend with random unused letters

            int innerScore = subGame->evalBoardsForward(); //NOTE: at this call the rack has a random prefill after removing the outer word
            string innerWord = subGame->bestWord;

            int newScore = outerScore - innerScore;
            if (newScore < strongestCounterScore) {
                strongestCounterWord = innerWord;
                strongestCounterScore = newScore;
            }

            if (Common::debug > 4) {
                ostringstream ostr;
                if (subGame->bestAxis == AXIS::VERTICAL) {
                    ostr << (char) ('A' + subGame->bestCol);
                    ostr << setw(2) << setfill('0') << subGame->bestRow + 1;
                } else {
                    ostr << setw(2) << setfill('0') << subGame->bestRow + 1;
                    ostr << (char) ('A' + subGame->bestCol);
                }
                cout << "<<" << *ident(nCount, numTopNToTry, tries + 1) << "evalAll(" << outerWord << "=" << setw(3)
                << setfill('0') << outerScore << ' ' << ostr.str() << " value=" << setw(4) << setfill(' ')
                << newScore << " [rack=" << subGame->rack << "][innerScore=" << setw(4) << innerScore
                << "][innerWord=" << setw(10) << innerWord << "][bestInnerScore=" << setw(4)
                << strongestCounterScore << "]";
                if (bestChosenScore!=INT_MIN)
                    cout << "[bestOuterWord=" << bestChosenWord << "][bestCountermove=" << setw(10) << bestCountermove
                    << "][bestChosenScore=" << setw(4) << bestChosenScore << "]";
                cout << endl;
            } else if (Common::debug > 0) {
                cout << "<<" << *ident(nCount, numTopNToTry, tries + 1) << "evalAll(" << outerWord << ", " << innerWord
                << ")" << endl;
            }

            if (strongestCounterScore < bestChosenScore) {
                cout << "<<" << *ident(nCount, numTopNToTry, tries + 1) << "prune " << setfill(' ') << outerWord
                << " due to racked word " << strongestCounterWord << " (value " << strongestCounterScore
                << ") since " << bestChosenWord << " gives " << bestChosenScore << endl;
                break;
            }
        }
        rack.assign(gameRacks.at(player));
        if (strongestCounterScore > bestChosenScore) {
            bestChosenScore = strongestCounterScore;
            bestChosenWord = outerWord;
            bestCountermove = strongestCounterWord;
        }

        cout << "<<" << *ident(nCount, numTopNToTry) << "minmax [bestChosenWord=" << setw(10) << setfill(' ')
        << bestChosenWord << "][expectedCounterMove=" << bestCountermove << "][minmaxValue=" << bestChosenScore
        << "]" << endl;

        subGame->board.restoreWord(it->getDirection(), board.getCell(outerRow, outerCol), cellBuffer);
    }
    cout << "**" << *ident(nCount, numTopNToTry, numRacksToTry + 1) << "best move at " << currLookahead << " is "
    << bestChosenWord << " with gain of " << bestChosenScore << endl;

    bestScore = bestChosenScore;
    bestWord = bestChosenWord;
    currLookahead--;
    return bestScore;
}

/* Method cementWordAndCleanRack adds testWord to the board, and also remove any utilized letter from the rack. */
void GameManager::cementWordAndCleanRack(const string& testWord, const AXIS::Axis testAxis, const int testRow,
        const int testCol, Cell* copyBuff) {
    ubyte ccount[Common::numCharValues];
    memset(ccount, 0, Common::numCharValues * sizeof(ubyte));
    Cell *cell = board.getCell(testRow, testCol);
    int incr = board.getIncrement(testAxis);

    for (string::const_iterator it = testWord.begin(); it != testWord.end(); it++) {
        if (!cell->isCovered()) { //Get count of how many time to remove the letters that were taken from the rack
            ccount[*it - 'A']++;
        }
        //Use fixed sized increment to copy/restore all the cells (including assertion status)
        //The alternative design would copy only the changed letters, but that would mean saving/restoring more state
        *copyBuff++ = *cell;
        cell->set(*it);
        cell->assertCovered();
        cell += incr;
    }
    copyBuff->letter = 0;

    // Copy the rack over onto itself, skipping the instances that were consumed in the testWord
    string::iterator outIt = rack.begin();
    for (string::const_iterator it = rack.begin(); it != rack.end(); it++) {
        if (ccount[*it - 'A']-- <= 0)
            *outIt++ = *it;
    }
    rack.resize(outIt - rack.begin());
}

void GameManager::extendRack(uint siz, string &basis) {
    if (rack.size() < siz) {
        uint deltaSize = siz - rack.size();
        if (deltaSize > basis.size())
            deltaSize = basis.size();
        random_sample_n(basis.c_str(), basis.c_str() + basis.size(), std::back_inserter(rack), deltaSize);
    }
}

/***
 * Collect the letters that are not part of any rack or the board itself.
 * This method cannot be put, as is, into the Board class since class
 * does not know about the racks.
 */
void GameManager::getAvailableLetters(string &res) {
    res.clear();
    byte nchar[Common::numCharValues];
    memcpy(nchar, Common::letterCount, Common::numCharValues * sizeof(byte));
    // Remove letters that are on the board
    for (Cell *c = board.firstSquare(); c < board.lastSquare(); c++) {
        if (c->isFilled()) {
           if( nchar[c->getLetter() - 'A']-- < 0) {
                cerr << "Should not happen -- negative count(1) for " << board.describeCell(c) << endl;
            }
        }
    }
    // Remove letters that are in a rack?
    int currPlayer = 0;
    for (vector<string>::iterator it = gameRacks.begin(); it < gameRacks.end(); it++) {
        if(currPlayer++==player)continue;
        for (string::iterator itt = (*it).begin(); itt < (*it).end(); itt++) {
             cout << "Racked char = " << *itt << " precount=" << (int) nchar[toupper(*itt - 'A')];
            if( nchar[toupper(*itt) - 'A']-- < 0) {
                 cerr << "Should not happen -- negative count(2) for rack " << *it << endl;
             }
             cout << " postcount=" << (int) nchar[*itt - 'A'] << endl;
        }
    }
    // Collect letters that are still available
    for (int i = 0; i < Common::numCharValues - 1; i++)
        for (int j = 0; j < nchar[i]; j++)
            res.push_back('A' + i);
}
