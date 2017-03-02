/*
 * Axis.cpp
 *
 *  Created on: Aug 13, 2009
 *      Author: mlrus
 */

#include "Axis.h"

using namespace AXIS;

const std::string& AXIS::toString(Axis axis) {
    switch (axis) {
        case VERTICAL:
            return Common::vertical;
        case HORIZONTAL:
            return Common::horizontal;
        case POINT:
            return Common::point;
        case INVALID:
            return Common::invalid;
        default:
            return Common::err;
    }
}

const std::string AXIS::mkLabel(Axis axis, uint row, uint col) {
    std::ostringstream ostr;
    if(axis==AXIS::VERTICAL) {
        ostr << (char) ('A'+col);
        ostr << std::setw(2) << std::setfill('0') << row+1;
    } else {
        ostr << std::setw(2) << std::setfill('0') << row+1;
        ostr << (char)('A'+col);
    }
    return ostr.str();
}

