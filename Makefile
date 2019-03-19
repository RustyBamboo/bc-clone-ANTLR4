all: antlr4 java run

build: antlr4 java

antlr4: calculator.g4
	antlr4 calculator.g4 -visitor

java: 
	javac *.java

run:
	./auto-test.sh


