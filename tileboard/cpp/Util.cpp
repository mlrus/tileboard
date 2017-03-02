/*
 * Util.cpp
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#include "ctype.h"
#include "limits.h"

#include <fstream>
#include <iostream>
#include <list>
#include <set>
#include <string>

#include "Util.h"

vector<string> Util::split(string& s, char c) {
    vector<string> res;
    size_t pos = 0;
    size_t found;
    while ((found = s.find(c, pos)) != string::npos) {
        string st = s.substr(pos, found - pos);
        trim(st);
        if (st.size() > 0) {
            res.push_back(string(st));
        }
        pos = found + 1;
    }
    if (pos < s.size()) {
        string st = s.substr(pos);
        trim(st);
        if (st.size() > 0) {
            res.push_back(string(st));
        }
    }
    return res;
}

int compare(const char *a, const char *b) {
    return *a - *b;
}

string Util::lexify(string& in) {
    char *dat = new char[in.size() + 1];
    memcpy(dat, in.data(), in.size());
    qsort(dat, in.size(), sizeof(char), (int(*)(const void*, const void*)) &compare);
    dat[in.size()] = 0;
    string ans = string(dat);
    delete[] dat;
    return string(ans);
}

void Util::trim(string& str) {
    size_t start = str.find_first_not_of(" \t\r\n");
    size_t end = str.find_last_not_of(" \t\r\n");
    if (string::npos == start || string::npos == end)
        str = "";
    else
        str = str.substr(start, end - start + 1);
}

//Lower case (in place)
void Util::lowerCase(string& in) {
    for (size_t i = 0; i < in.size(); i++)
        in[i] = tolower(in[i]);
}

string Util::toLower(string& in) {
    string s(in);
    lowerCase(s);
    return s;
}

void Util::upperCase(string& in) {
    for (size_t i = 0; i < in.size(); i++)
        in[i] = toupper(in[i]);
}

string Util::toUpper(string& in) {
    string s(in);
    upperCase(s);
    return s;
}

static bool isBlankChar(char c) {
    return (c == ' ');
}

string Util::removeSpaces(string& s) {
    string out;
    remove_copy_if(s.begin(), s.end(), back_inserter(out), &isBlankChar);
    return out;
}

set<string> Util::readFile(const string& filename) {
    set<string> wordSet;
    return readFile(filename, wordSet);
}

set<string> Util::readFile(const string& filename, set<string>&wordSet) {
    try {
        ifstream infile(filename.c_str(), std::ios_base::in);
        string line;
        while (getline(infile, line, '\n')) {
            trim(line);
            upperCase(line);
            wordSet.insert(line);
        }
        infile.close();
    } catch (...) {
        cerr << "Error reading " << filename << endl;
    }
    return wordSet;
}
