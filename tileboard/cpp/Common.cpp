/*
 * Common.cpp
 *
 *  Created on: Jul 6, 2009
 *      Author: mlrus
 */
#include "Common.h"

uint Common::debug = 0;
const int Common::roundingFactorINT = 10000;
const float Common::roundingFactorDBL = (float)Common::roundingFactorINT;
bool Common::emitAllPotentialWords = false;
std::string* Common::EMPTYSTRINGPTR = const_cast<std::string*> (&Common::EMPTYSTRING);
uint Common::numToDisplay = 10;
float Common::considerTopPercentile = 0.25f;
const uchar Common::EMPTYCHAR = '_';
const byte Common::letterCount[Common::numCharValues] = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};//, 2 };
const std::string Common::VALIDCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
const std::string Common::EMPTYSTRING;
const std::string Common::vertical   = "vertical";
const std::string Common::horizontal = "horizontal";
const std::string Common::point      = "point";
const std::string Common::invalid    = "invalid";
const std::string Common::err        = "error";
