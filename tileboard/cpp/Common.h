/*
 * Common.h
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#ifndef COMMON_H_
#define COMMON_H_

#include <string>

typedef u_int32_t uint;
typedef u_int16_t ushort;
typedef u_int8_t uchar;
typedef u_int8_t ubyte;
typedef int8_t byte;

#define _USE_HASH_TABLE_

#define DEBUG_ENABLE
#undef DEBUG_ENABLE

class Common {

    public:
        static const uchar EMPTYCHAR;
        static const std::string VALIDCHARS;
        static const std::string EMPTYSTRING;
        static std::string* EMPTYSTRINGPTR;
        static uint debug;
        static bool emitAllPotentialWords;
        static const int resultReservationSize = 10000;
        static uint numToDisplay;
        static float considerTopPercentile; //what percentile to look at when considering the best average move?
        static const int boardSize = 15;
        static const int roundingFactorINT;
        static const float roundingFactorDBL;
        static const uint rackSize = 7;        // Max number of letters to add in one move
        static const uint bingoValue = 50;
        static const int maxRackBreadth = 15; // Max number of letter tractable for one move.
        static const u_int32_t ANYLETTER = 0xFFFFFFFF; //~0xF8000000;

        static const std::string vertical;
        static const std::string horizontal;
        static const std::string point;
        static const std::string invalid;
        static const std::string err;

        // The twenty-seventh entry is the blank tiles, of which there are two
        // static const byte letterCount[27];
        // static const int numCharValues=27;
        static const int numCharValues=26;
        static const byte letterCount[numCharValues];


        Common() {
        }

        virtual ~Common() {
        }

};

#endif /* COMMON_H_ */
