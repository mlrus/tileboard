/*
 * Result.h
 *
 *  Created on: Jul 17, 2009
 *      Author: mlrus
 */

#ifndef RESULT_H_
#define RESULT_H_

#include "Axis.h"

class Result {
    public:
        string word;
        uint score;
        uint row, col;
        AXIS::Axis direction;

        const string toString() const {
            ostringstream str;
            str << setw(5) << score << " " << setw(15) << word << " ";
            if (direction == AXIS::VERTICAL) {
                str << Common::VALIDCHARS[col] << setfill('0') << setw(2) << (row + 1);
            } else {
                str << setfill('0') << setw(2) << (row + 1) << Common::VALIDCHARS[col];
            }
            return str.str();
        }

        const string& getWord() const {
            return word;
        }

        const uint getScore() const {
            return score;
        }

        const uint getRow() const {
            return row;
        }

        const uint getCol() const {
            return col;
        }

        const AXIS::Axis getDirection() const {
            return direction;
        }

        Result(string word, uint score, uint row, uint col, AXIS::Axis direction) :
            word(*new string(word)), score(score), row(row), col(col), direction(direction) {
        }

        Result(const Result& result) :
            word(result.word), score(result.score), row(result.row), col(result.col), direction(result.direction) {
        }

        virtual ~Result() {
        }
};

#endif /* RESULT_H_ */
