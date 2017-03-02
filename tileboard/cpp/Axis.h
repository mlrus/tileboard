/*
 * Axis.h
 *
 *  Created on: Jul 17, 2009
 *      Author: mlrus
 */

#ifndef AXIS_H_
#define AXIS_H_

#include <sstream>
#include <iostream>
#include <iomanip>
#include <string>

#include "Common.h"

namespace AXIS {
    enum Axis {
        HORIZONTAL, VERTICAL, POINT, INVALID
    };

    inline static const Axis getDual(Axis axis) {
        return axis == HORIZONTAL ? VERTICAL : (axis == VERTICAL ? HORIZONTAL : POINT);
    }

    inline static const int getIncr(Axis axis) {
        return axis==AXIS::HORIZONTAL?1:Common::boardSize;
    }

    const std::string& toString(AXIS::Axis axis);
    const std::string mkLabel(AXIS::Axis axis, uint row, uint col);

}

#endif /* AXIS_H_ */
