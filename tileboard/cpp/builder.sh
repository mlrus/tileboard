#!/bin/bash
OPTS="-d /Users/mlrus/scrabbleTwo/dicts/TWL06.txt -b /Users/mlrus/scrabbleTwo/games/computer/game13/board.023  -nm 2 UAGTFKT"
COMP="g++  -fmessage-length=0  -O3 -mdynamic-no-pic -fvisibility=hidden -fvisibility-inlines-hidden -finline-limit=12000"

if [ 1 == 2 ]; then
  rm  *.gc*
  ${COMP} -fprofile-generate -o Scrabble++ *.cpp
  ./Scrabble++ ${OPTS}
  ${COMP} -fprofile-use -o Scrabble++ *.cpp
  ./Scrabble++ ${OPTS}
else
  rm  *.gc*
  ${COMP} -fprofile-generate -fprofile-arcs -fvpt -fspeculative-prefetching -o Scrabble++ *.cpp
  ./Scrabble++ ${OPTS}
  ${COMP} -fprofile-use -fbranch-probabilities -fvpt -fspeculative-prefetching -o Scrabble++ *.cpp
  ./Scrabble++ ${OPTS}
fi
