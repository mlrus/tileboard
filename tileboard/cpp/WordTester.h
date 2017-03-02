/*
 * Tester.h
 *
 *  Created on: Aug 16, 2009
 *      Author: mlrus
 */

#ifndef TESTER_H_
#define TESTER_H_

#include <string>
#include <vector>
#include <map>
#include <math.h>

#include "Common.h"
#include "Cell.h"
#include "Board.h"
#include "Dictionary.h"
#include "Axis.h"
#include "GameManager.h"

class WordTester {
    void operator=(const WordTester&);
    WordTester(const WordTester&);

    public:
        class L2Map;

        typedef vector<Cell*> cellvector;
        typedef pair<vector<Cell *> , vector<Cell *> > cellvectorPair;
        typedef pair<string, cellvectorPair> namedCellvectorPair;
        typedef vector<namedCellvectorPair> namedItemVector;

        class WordTestResult;

        void evaluatePotentialMatches(L2Map& results);
        void showPotentialMatches(L2Map& results, uint numResults = INT_MAX);
        bool matchCell(const char* word, Cell *cell, AXIS::Axis axis, L2Map& results);
        void matchRow(const char * word, L2Map& results);
        void matchCol(const char * word, L2Map& results);

        inline void needsRackChar(WordTestResult *wt, char c) {
            wt->updateProb(letterProbs.at(c - 'A'));
        }
        bool checkNewcellPerpendiculars(Cell *cellBuffer, Cell* cell, AXIS::Axis dual);

        inline WordTester(Scrabble *scr, string& letters) :
            scrabble(scr), letterProbs() {
            memset(ccount, 0, Common::numCharValues);
            memset(useCount, 0, Common::numCharValues);
            for (string::iterator it = letters.begin(); it != letters.end(); it++) {
                ccount[*it - 'A']++;
            }
            for (int i = 0; i < Common::numCharValues; i++) {
                float oneLetterP = 1.0f / letters.size();
                letterProbs.push_back(ccount[i] * oneLetterP);
            }
        }

        virtual ~WordTester() {
        }

        struct classcomp {
            inline bool operator()(const WordTestResult* w1, const WordTestResult* w2) const {
                if (w1->wlogit != w2->wlogit)
                    return (w1->wlogit < w2->wlogit);
                return strcmp(w1->word, w2->word) < 0;
            }
        };

        struct classcomp2 {
            inline bool operator()(const WordTestResult* w1, const WordTestResult* w2) const {
                //                if (w1->prob != w2->prob)
                //                    return (w1->prob > w2->prob);
                if (w1->score != w2->score)
                    return (w1->score > w2->score);
                return strcmp(w1->word, w2->word) > 0;
            }
        };

        Scrabble* scrabble;
        vector<float> letterProbs;
        byte ccount[Common::numCharValues];
        byte useCount[Common::numCharValues];

        class WordTestResult {

            void operator=(const WordTestResult&);

            public:
                friend class WordTester;

                Cell* cell;
                AXIS::Axis axis;
                const char* word;
                float prob;
                int score;
                float wlogit;

                WordTestResult(Cell *c, const AXIS::Axis axs, const char* w) :
                    cell(c), axis(axs), word(w), prob(1.0f), score(0), wlogit(0.0f) {
                }

                inline WordTestResult(const WordTestResult& wtr) :
                    cell(wtr.cell), axis(wtr.axis), word(wtr.word), prob(wtr.prob), score(wtr.score),
                    wlogit(wtr.wlogit) {
                }

                inline void updateProb(float letterProb) {
                    prob *= letterProb;
                }

                virtual ~WordTestResult() {
                    // Do not delete cell or word since they are already references
                }
        };

        class L2Map {

            L2Map(const WordTester::L2Map&);
            void operator=(const WordTester::L2Map&);

            typedef map<string, vector<WordTester::WordTestResult *> > L2MAP;
            typedef pair<vector<WordTester::WordTestResult*> , string> RVECLMT;
            Cell *topCell;
            AXIS::Axis topAxis;
            string topWord;
            string topChars;
            float topProb;
            int topScore;
            int position;
            L2MAP l2map;
            vector<RVECLMT*> resequenced;

            void resequenceItems();
            void pickfromTopPct();
            void emit(const Board& board, size_t numitems);

            public:
                void showItems(const Board& board);
                void emitChain(const Board& board, vector<WordTestResult *>& v);
                void clear() {
                    topCell = NULL;
                    topAxis = AXIS::INVALID;
                    topWord.clear();
                    topChars.clear();
                    topProb = 0.0f;
                    topScore = 0;
                    position = 0;
                    l2map.clear();
                    resequenced.clear();
                }

                void addItem(vector<char> keyVec, WordTester::WordTestResult* wtr);


                L2Map() :
                    topCell(), topAxis(), topWord(), topChars(), topProb(0.0f), topScore(0), position(0), l2map(),
                    resequenced() {
                }

                virtual ~L2Map() {
                    resequenced.clear();
                    l2map.clear();
                }

                const Cell* getTopCell() {
                    return topCell;
                }
                const AXIS::Axis getTopAxis() {
                    return topAxis;
                }
                const string getTopWord() {
                    return topWord;
                }
                const string getTopChars() {
                    return topChars;
                }
                const int getTopScore() {
                    return topScore;
                }
                const float getTopProb() {
                    return topProb;
                }
                const int getPos() {
                    return position;
                }

                void showItemsResequenced(const Board& board, uint numitems = INT_MAX);

                struct paircomp {
                    inline bool operator()(const RVECLMT* w1, const RVECLMT* w2) const {
                        float p1 = w1->first.at(0)->prob;
                        float p2 = w2->first.at(0)->prob;
                        if (p1 != p2) {
                            return p1 < p2;
                        }
                        int s1 = w1->first.at(0)->score;
                        int s2 = w2->first.at(0)->score;
                        if (s1 != s2) {
                            return s1 < s2;
                        }
                        const char * str1 = w1->first.at(0)->word;
                        const char * str2 = w2->first.at(0)->word;
                        return strcmp(str1, str2) < 0;
                    }
                };
        };
};

#endif /* TESTER_H_ */
