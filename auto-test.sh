#!/bin/bash

# Name of program
lexer_name="calculator"
# Current directory
where=$(pwd)

# Percision
scale=16

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
CYAN='\033[1;36m'
C='\033[1;37m'
NC='\033[0m'

passed=0
failed=0
for entry in test/*.bc
do
    cd "$where"
    txt=$(cat $entry)
    printf "${CYAN}---- Running test $entry ----${NC}\n"
    bc_output=$(bc -l <(echo "scale=${scale}"; cat $entry; printf "quit"))
    antlr_output=$(java calculator "$entry")
    
    if [ "$bc_output" == "$antlr_o_antlr_output" ]
    then
 #       printf "${GREEN}PASS:${NC} $entry\n"
        passed=$((passed+1))
    else
 #       printf "${RED}FAILED:${NC} $entry\n"
        failed=$((failed+1))
    fi

    printf "${C}Expected (from bc):${NC}\n$bc_output\n"
    printf "${C}Got (our program):${NC}\n$antlr_output\n"
#    printf "$txt\n/*\nExpected (from bc):\n$bc_output\n Got (our program):\n$antlr_output\n*/" > "$where/$entry"

done

#printf "${CYAN}---- Result: ----${NC}\n"
#printf "Passed: ${GREEN}$passed ${NC}\n"
#printf "Failed: ${RED}$failed ${NC}\n"


