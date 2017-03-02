#ifndef CELL_H_
#define CELL_H_

#include <sstream>
#include <iostream>
#include <ostream>
#include <iomanip>
#include <string>
#include <iomanip>
#include <ctype.h>

#include "Common.h"
#include "Axis.h"

class Cell {
    friend class Board;
    friend class Scrabble;
    friend class GameManager;

    static const ubyte UBYTE_1 = 1;
    static const ubyte UBYTE_2 = 2;
    static const ubyte UBYTE_3 = 3;
    static const uchar letterValues[26];

    Cell(const Cell& cell) :
        letter(cell.letter),
        rowZero(cell.rowZero),
        rowN(cell.rowN),
        colZero(cell.colZero),
        colN(cell.colN),
        covered(cell.covered),
        value(cell.value),
        wordFactor(cell.wordFactor),
        letterFactor(cell.letterFactor),
        validVertChars(cell.validVertChars),
        validHoriChars(cell.validHoriChars) {
    }

    Cell& operator=(const Cell& cell) {
        letter = cell.letter;
        rowZero = cell.rowZero;
        rowN = cell.rowN;
        colZero = cell.colZero;
        colN = cell.colN;
        covered = cell.covered;
        value = cell.value;
        wordFactor = cell.wordFactor;
        letterFactor = cell.letterFactor;
        validVertChars = cell.validVertChars;
        validHoriChars = cell.validHoriChars;
        return *this;
    }

    uchar letter;
    bool rowZero, rowN, colZero, colN, covered;
    ubyte value;
    ubyte wordFactor;
    ubyte letterFactor;
    u_int32_t validVertChars;
    u_int32_t validHoriChars;

    public:

        Cell() :
            letter(0),
            rowZero(false),
            rowN(false),
            colZero(false),
            colN(false),
            covered(false),
            value(0),
            wordFactor(0),
            letterFactor(1),
            validVertChars(0),
            validHoriChars(0) {
        }

        virtual ~Cell() {
        }

        const inline u_int32_t getValidChars(AXIS::Axis axis) const {
            return axis == AXIS::HORIZONTAL ? validHoriChars : validVertChars;
        }

        inline bool operator==(const Cell& other) const {
            return letter == other.letter;
            //&& value==other.value
            //&& wordFactor==other.wordFactor
            //&& letterFactor==other.letterFactor;
        }

        inline bool operator!=(const Cell& other) const {
            return !operator==(other);
        }

        bool validFor(const uchar ch, const AXIS::Axis axis) const {
            return getValidChars(axis) & 0x1 << (ch - 'A');
        }

        inline Cell*set(uchar letter) {
            this->letter = toupper(letter);
            if (isupper(letter))
                this->value = letterValues[letter - 'A'];
            else
                this->value = 0;
            return this;
        }

        inline void assertCovered() {
            wordFactor = 1;
            letterFactor = 1;
            covered = true;
        }

        void setValidChars(u_int32_t validChars, AXIS::Axis axis) {
            if (axis == AXIS::HORIZONTAL)
                validHoriChars = validChars;
            else
                validVertChars = validChars;
        }

        inline const uchar getLetter() const {
            return letter;
        }

        inline const ubyte getValue() {
            return value;
        }

        inline void clear() {
            letter = Common::EMPTYCHAR;
            value = 0;
            //isFilled = false;
        }

        inline const bool isEmpty() const {
            return letter == Common::EMPTYCHAR;
        }

        inline const bool isFilled() const {
            return !isEmpty();
        }

        inline const bool isCovered() const {
            return covered;
        }

        inline const bool isValid() const {
            return isFilled() && ((letter >= 'A' && letter <= 'Z') || letter == Common::EMPTYCHAR);
        }

        inline const bool isEndpoint(int direction) const {
            if (direction == -1)
                return colZero;
            if (direction == -Common::boardSize)
                return rowZero;
            if (direction == 1)
                return colN;
            if (direction == Common::boardSize)
                return rowN;
            return false;
        }

        inline Cell* nextCell(int direction) {
            return this + direction;
        }

        const char *showType() const {
            if (wordFactor == UBYTE_1 && letterFactor == UBYTE_1)
                return " ";
            if (wordFactor == UBYTE_1 && letterFactor == UBYTE_3)
                return "d";
            if (wordFactor == UBYTE_1 && letterFactor == UBYTE_3)
                return "t";
            if (wordFactor == UBYTE_3)
                return ("T");
            if (wordFactor == UBYTE_2)
                return ("D");
            return "??";
        }

        std::string toString() const {
            std::ostringstream str;
            if (letter == Common::EMPTYCHAR)
                str << "          ";
            else {
                str << letter << ':' << showType() << " " << std::setw(2) << value << ':' << wordFactor << ':'
                << letterFactor;
            }
            return str.str();
        }
};

#endif /*CELL_H_*/
