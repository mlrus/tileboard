/*
 * Util.h
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#ifndef UTIL_H_
#define UTIL_H_

#include <set>
#include <string>
#include <vector>

using namespace std;

class Util {

    public:

        Util() {
        }

        ~Util() {
        }

        static int indexOf(const char c);
        static set<string> readFile(const string& filename);
        static set<string> readFile(const string& filename, set<string>&words);
        static string lexify(string& in);
        static string removeSpaces(string &s);
        static string toLower(string& in);
        static string toUpper(string& in);
        static vector<string> split(string& s, char c = ' ');
        static void lowerCase(string& in);
        static void trim(string& str);
        static void upperCase(string& in);
};

#endif /* UTIL_H_ */
