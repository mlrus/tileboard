//============================================================================
// Name        : Jav.cpp
// Author      : Michah Lerner
// Version     :
// Copyright   : Free for anyone
// Description : Hello World in C++, Ansi-style
//============================================================================
//
//-Winline -finline-functions  -finline-limit=64578
//-c -fmessage-length=0 -fno-non-lvalue-assign -Weffc++ -Wabi -Wold-style-cast -Woverloaded-virtual -Wsign-promo
//
//
#include <iostream>
#include <stdlib.h>
#include <vector>

#include "Clocker.h"
#include "Common.h"
#include "Board.h"
#include "Dictionary.h"
#include "Scrabble.h"
#include "GameManager.h"
#include "WordTester.h"

using namespace std;

string boardName;
string dictName;

int minimaxDepth=0, topNCount=3, numRacksTry=5, rackPoolSize=Common::rackSize, nPlayers=2;

string testWord;
AXIS::Axis testAxis;
int testRow=-1,testCol=-1;

static char ** processArgs(char **arg, char **lastArg) {
    while (arg < lastArg) {
        if (strcmp(*arg, "-h") == 0) {
            cout << "prog [<minimax>] -d dict -b board rack" << endl;
            cout << "      <minimax> :: [-nP numplayers] [-nm minimaxDepth] [-nt topNCount] [-nr numRacksToTry] [-np rackPoolSize] [-debug XX]" << endl;
            cout << "      [-t word axis testRow testCol]  where axis is H or anything else" << endl;
            cout << "      [-wTester] to activate the full testing of all possible word"<<endl;
            cout << "      [-nD XX] to set the number of results to display for each lookahead trie" <<endl;
            cout << "      [-nPct XX] percentile to consider when looking for highest score" << endl;
            return NULL;
        }
        if(strcmp(*arg, "-debug")==0){ arg++; Common::debug = atoi(*arg++); continue; }
        if(strcmp(*arg, "-wTester")==0) { arg++; Common::emitAllPotentialWords=!Common::emitAllPotentialWords; }
        if(strcmp(*arg, "-nm")==0)   { arg++; minimaxDepth = atoi(*arg++); continue; }
        if(strcmp(*arg, "-nt")==0)   { arg++; topNCount = atoi(*arg++); continue; }
        if(strcmp(*arg, "-nr")==0)   { arg++; numRacksTry = atoi(*arg++); continue; }
        if(strcmp(*arg, "-np")==0)   { arg++; rackPoolSize = atoi(*arg++); continue; }
        if(strcmp(*arg, "-nP")==0)   { arg++; nPlayers = atoi(*arg++); continue; }
        if(strcmp(*arg, "-nD")==0)   { arg++; Common::numToDisplay = atoi(*arg++); continue; }
        if(strcmp(*arg, "-nPct")==0) { arg++; Common::considerTopPercentile = (float)atof(*arg++); continue; }
        if (strcmp(*arg, "-d") == 0) { arg++; dictName = string(*arg++); continue; }
        if (strcmp(*arg, "-b") == 0) { arg++; boardName = string(*arg++); continue; }
        if (strcmp(*arg, "-t") == 0) {
            arg++;
            testWord = string(*arg++);
            testAxis = string(*arg++)[0]=='H'?AXIS::HORIZONTAL:AXIS::VERTICAL;
            testRow = atoi(*arg++);
            testCol = atoi(*arg++);
            cout << "Test \""<<testWord<<"\" " << AXIS::toString(testAxis) << " from " << testRow << ", " << testCol << endl;
            continue;
        }
        break;
    }
    if(Common::numToDisplay<0)Common::numToDisplay=INT_MAX;
    cout << "settings:"
    <<  "\n     -d "<<dictName
    <<  "\n     -b "<<boardName
    <<  "\n     -nm [minMaxDepth] "<<minimaxDepth
    <<  "\n     -nt [topNCount] "<<topNCount
    <<  "\n     -nr [numRacksTry] "<<numRacksTry
    <<  "\n     -np [rackPoolsSize] "<<rackPoolSize
    <<  "\n     -nP [nPlayers] "<<nPlayers
    <<  "\n     -nD [nDisplay] "<<Common::numToDisplay
    <<  "\n     -nPct [nPercentile] " << Common::considerTopPercentile
    <<  "\n     " <<*arg << endl;

    return arg;
}
struct eqstr
{
    bool operator()(const char* s1, const char* s2) const
    {
        return strcmp(s1, s2) == 0;
    }
};

int main(int argc, char **argv) {
    sranddev();
    Clocker clock;
    string rackString;
#ifdef _USE_HASH_TABLE_
    cout << "\nUsing USE_HASH_TABLE dictionary" << endl;
#else
    cout << "\nUsing set dictionary" << endl;
#endif
    char **nextArg = processArgs(argv + 1, argv + argc);
    if(nextArg == NULL) return -1;
    if (dictName.size() == 0) {
        cerr << "Missing: -d <dictionary>" << endl;
        return -1;
    }

    if (boardName.size() == 0) {
        cerr << "Missing: -b <board>" << endl;
    }

    if (nextArg < argv + argc) {
        rackString = string(*nextArg++);
    }

    cout << "dictionaryName="<<dictName<<endl;
    cout << "boardName="<<boardName<<endl;
    cout << "rack="<<rackString<<endl;

    Board board(boardName);
    Dictionary dictionary(dictName);


    Scrabble *scrabble = new Scrabble(&dictionary, board, rackString);
    cout << "\nNOTE: EXECUTION BEGINS " << clock.readResetDetailed() << endl;
    scrabble->initCellChars();
    if(testWord.size()) {
        cout << "Test word: " << testWord << endl;
        board.addWord(testWord,testAxis,testRow,testCol);
        cout << board.formatBoard() << '\n' << board.scoreMove(board.getCell(testRow, testCol),testAxis) << endl;
        return 0;
    }

    if(minimaxDepth==0) {
        scrabble->evalBoardCurrent();
        scrabble->showResults();
    } else  {
        cout << "minimaxDepth="<<minimaxDepth
        <<" toNCount="<<topNCount
        <<" numRacksTry="<<numRacksTry
        <<" rackPoolSize"<<rackPoolSize<<endl;
        GameManager *gameManager = new GameManager(scrabble);
        gameManager->setMaxlookahead(minimaxDepth);
        gameManager->setTopNCount(topNCount);
        gameManager->setNumRacksToTry(numRacksTry);
        gameManager->setRackPoolSize(rackPoolSize);
        gameManager->setNumPlayers(nPlayers);
        gameManager->setRack(0,gameManager->rack);
        for(int i=1;i<nPlayers;i++)
            gameManager->setRack(i,*(new string("")));

        if(Common::emitAllPotentialWords) {
            string availableLetters;
            gameManager->getAvailableLetters(availableLetters);
            WordTester tester(scrabble,availableLetters);

            vector<WordTester::WordTestResult*> results;
            WordTester::L2Map lmapresult;
            cout << "\nBegin checking all potential matches: " << clock.readResetDetailed() << endl;
            tester.evaluatePotentialMatches(lmapresult);
            cout << "\nDone checking all potential matches: " << clock.readResetDetailed() << endl;
            tester.showPotentialMatches(lmapresult);
        }

        if(true) gameManager->evalAndCountermove();
        else gameManager->evalBoardsForward();

    }
    cout << endl;
    cout << "NOTE: EXECUTION COMPLETE " << clock.readResetDetailed() << endl;
    cout << "NOTE: dictionaryName="<<dictName<<endl;
    cout << "NOTE: boardName="<<boardName<<endl;
    cout << "NOTE: rack="<<rackString<<endl;

#ifdef _USE_HASH_TABLE_
    cout << "Used USE_HASH_TABLE dictionary" << endl;
#else
    cout << "Used set dictionary" << endl;
#endif
    return 0;
}

