/*
 * GameManager.h
 *
 *  Created on: Aug 13, 2009
 *      Author: mlrus
 */

#ifndef GAMEMANAGER_H_
#define GAMEMANAGER_H_

#include "Scrabble.h"

class GameManager: public Scrabble {
        void operator=(const GameManager&);
        GameManager(const Scrabble&);

    public:
        int currLookahead, maxLookahead;
        int numTopNToTry, numRacksToTry, rackMaxPool;
        int nPlayers;
        int player;
        vector<string> gameRacks;
        string workrack;

        GameManager(const Scrabble* scr) :
            Scrabble(scr), currLookahead(0), maxLookahead(0), numTopNToTry(0), numRacksToTry(0), rackMaxPool(0),
                    nPlayers(1), player(0), gameRacks(), workrack() {
        }

        /***
         * Whatever is currently the rack becomes the stored rack
         * of the current (pre-incremented) player.
         */
        GameManager(const GameManager* mgr) :
            Scrabble(mgr), currLookahead(mgr->currLookahead), maxLookahead(mgr->maxLookahead), numTopNToTry(
                    mgr->numTopNToTry), numRacksToTry(mgr->numRacksToTry), rackMaxPool(mgr->rackMaxPool), nPlayers(
                    mgr->nPlayers), player((mgr->player + 1) % mgr->nPlayers), gameRacks(mgr->gameRacks), workrack(
                    string(gameRacks.at(player))) {

            for (vector<string>::iterator it = gameRacks.begin(); it != gameRacks.end(); it++) {
                *it = string(*it);
            } // Deep copy for racks
            gameRacks.at(mgr->player).assign(mgr->rack); // OK: previously active player gets whats left of the rack
            rack = gameRacks.at(player); // currently active rack gets the currently active player's rack
        }

        virtual ~GameManager() {
        }

        int evalBoardsForward();

        int evalAndCountermove();

        void setMaxlookahead(int value) {
            maxLookahead = value;
        }
        void setTopNCount(int value) {
            numTopNToTry = value;
        }
        void setNumRacksToTry(int value) {
            numRacksToTry = value;
        }
        void setRackPoolSize(int value) {
            rackMaxPool = value;
        }

        /* Method cementWordAndCleanRack updates the board with the word, and updates the rack to remove newly borded letters */
        void cementWordAndCleanRack(const string& testWord, const AXIS::Axis testAxis, const int testRow,
                const int testCol, Cell* copyBuff);

        void getAvailableLetters(string &res);

        /* Method extendRack set this->rack to the extended value of the current player's rack */
        void extendRack(uint siz, string &basis);

        auto_ptr<string> ident(int nCount);
        auto_ptr<string> ident(int nCount, int maxCount);
        auto_ptr<string> ident(int nCount, int maxCount, int rackNo);

        void setNumPlayers(int val) {
            nPlayers = val;
            gameRacks.assign(nPlayers, Common::EMPTYSTRING);
        }

        void setRack(int n, string& rack) {
            gameRacks.at(n) = rack;
        }

        inline void useCurrentPlayer() {
            rack.assign(gameRacks.at(player));
        }

        inline void setWorkdRack() {
            workrack.assign(rack);
        }
        inline void useWorkRack() {
            rack.assign(workrack);
        }
};

#endif /* GAMEMANAGER_H_ */
