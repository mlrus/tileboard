/*
 * Dictionary.h
 *
 *  Created on: Jun 28, 2009
 *      Author: mlrus
 */

#ifndef DICTIONARY_H_
#define DICTIONARY_H_

#include <ext/hash_set>
#include <fstream>
#include <iostream>
#include <set>
#include <string>
#include <vector>

#include "Util.h"
#include "Storable.h"

class Dictionary: public Storable {

#ifndef _USE_HASH_TABLE_
    set<string> words;
    inline const bool lookup(const string &entry) {
        return words.count(entry) == 1;
    }

    inline const bool lookup(const char *entry) {
        return words.count(entry) == 1;
    }
#else

    public: // only public for testing.

    struct eqstr
    {
        bool operator()(const char* s1, const char* s2) const
        {
            return strcmp(s1, s2) == 0;
        }
    };

    __gnu_cxx::hash_set<const char*,__gnu_cxx:: hash<const char*>, eqstr> words;

    inline const bool lookup(const __gnu_cxx::hash_set<const char*, __gnu_cxx::hash<const char*>, eqstr>& words,
            const char* word)
    {
        __gnu_cxx::hash_set<const char*, __gnu_cxx::hash<const char*>, eqstr>::const_iterator it = words.find(word);
        return it!=words.end();
    }

    inline const bool lookup(const string& word) {
        return lookup(words,word.c_str());
    }

    inline const bool lookup(const char *word) {
        return lookup(words,word);
    }


    typedef __gnu_cxx::_Hashtable_const_iterator<const char*, const char*, __gnu_cxx::hash<const char*>, std::_Identity<const char*>, Dictionary::eqstr, std::allocator<const char*> > dictionaryHash;

#endif

    public:

        inline const bool contains(const string &entry) {
            return lookup(entry);
        }

        inline const bool contains(const char *entry) {
            return lookup(entry);
        }

        void clear() {
            words.clear();
        }

        inline int wordCount() {
            return words.size();
        }

        void read(const std::string &filename) {
            try {
                ifstream infile(filename.c_str(), std::ios_base::in);
                string line;
                while (getline(infile, line, '\n')) {
                    Util::trim(line);
                    Util::upperCase(line);
#ifdef _USE_HASH_TABLE_
                    words.insert(strdup(line.c_str()));
#else
                    words.insert(line);
#endif
                }
                infile.close();
            } catch (...) {
                cerr << "Error reading " << filename << endl;
            }
        }

        Dictionary() :
            words() {

        }
        Dictionary(string &filename) :
            words() {
            read(filename);
        }

        virtual ~Dictionary() {
            words.clear();
        }
};

#endif /* DICTIONARY_H_ */
