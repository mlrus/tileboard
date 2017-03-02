/*
 * Storable.h
 *
 *  Created on: Jun 29, 2009
 *      Author: mlrus (based on Stroustrop's storable abstract class)
 */

#ifndef STORABLE_H_
#define STORABLE_H_

#include <string>

class Storable {
    std::string name;
    public:
        Storable(const std::string& name = std::string()) :
            name(name) {
        }

        virtual void read(const std::string& name = std::string())=0; //pure virtual (an abstract class)
        virtual void write(const std::string& name = std::string()) { std::cout << name << std::endl; }

        virtual ~Storable() {
        }

};

#endif /* STORABLE_H_ */
