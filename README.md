# BC clone using ANTLR4

Simply run `make`.
This will access Makefile and do the following:
- Generate ANTLR4 lexer files (antlr4 calculator.g4 -visitor)
- Compile java (javac *.java)
- Run automated test script (./auto-test.sh)

# Code Layout:
- calculator.g4 contains ANTLR4 Grammar
- calculator.java contains main function that read in a file opens ANTLR4 parsetree
- CalculatorVisitor.java contains all visitor functions and respective handling
- Functions.java contains a wrapper to save parameters and execution block and contains a method to call function
- FunctionVisitor.java saves all functions 
- Env.java contains a wrapper to save Environment
- Break.java exception class to handle return statements
- auto-test.sh is a bash script to automatically run bc as well as our program and print both results
- test directory contains all test files

# Tests:
- test1.bc: assignment and print list
- test2.bc: assignment and simple math operation
- test3.bc: bc special functions (s, c, l, e)
- test4.bc: order of operatiosn and parenthesis
- test5.bc: comments
- test6.bc: boolean logic
- test7.bc: function definition and calling
- test8.bc: recursive function call
- test9.bc: read
- test10.bc: while loop
- test11.bc: if else conditions
- test12.bc: for loop
