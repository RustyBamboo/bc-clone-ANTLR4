grammar calculator;

parse
 : block EOF
 ;

block
 : ( statement | funcDecl )* ( Return '('? expr ')'? (';')? )?
 ;

statement
 : assignment (';' | ';\n' | '\n')
 | funcCall (';' | ';\n' | '\n')
 | ifStatement
 | forStatement
 | whileStatement
 | exprPrint (';' | '\n' | ';\n')
 | '\n'
 ;

exprPrint
 : expr
 ;

assignment
 : Identifier '=' expr
 ;

funcCall
 : Identifier '(' exprList? ')' #identifierFuncCall
 | Print exprList     #printFuncCall
 ;

ifStatement
 : ifStat elseStat?
 ;

ifStat
 : If '(' expr ')' '{' block '}' ('\n')?
 ;

elseStat
 : Else '{' block '}' ('\n')?
 ;

funcDecl
 : Define Identifier '(' idList? ')' '{' block '}' End='\n'
 ;

forStatement
 : For '(' assignment ';' expr ';' assignment ')' '{' block '}'
 ;

whileStatement
 : While '(' expr ')' '{' block '}' '\n'?
 ;

idList
 : Identifier ( ',' Identifier )*
 ;

exprList
 : expr ( ',' expr )*
 ;

expr
 : '-' expr                                       #negateExpr
 | '!' expr                                       #notExpr
 | <assoc=right> expr '^' expr              #powerExpr
 | expr op=( '*' | '/' | '%' ) expr         #multExpr
 | expr op=( '+' | '-' ) expr               #addExpr
 | expr op=( '>=' | '<=' | '>' | '<' ) expr #compExpr
 | expr op=( '==' | '!=' ) expr             #eqExpr
 | expr '&&' expr                           #andExpr
 | expr '||' expr                           #orExpr
 | Number                                               #numberExpr
 | funcCall                                         #funcCallExpr
 | Identifier                                           #identifierExpr
 | '(' expr ')'                                   #exprExpr
 | 'read()'                                     #readExpr
 ;


Define      : 'define';
If       : 'if';
Else     : 'else';
Return   : 'return';
For      : 'for';
While    : 'while';
To       : 'to';
Print    : 'print';

EQUALS  : '==';
NOTEQUALS  : '!=';
GTEQUALS : '>=';
LTEQUALS : '<=';
GREATER       : '>';
LESSER       : '<';
ADD      : '+';
SUB : '-';
MUL : '*';
DIV   : '/';

ENDER: (('\r'? '\n') | ';' | ';\n' | '\n') ;

Number
 : Int ( '.' Digit* )?
 ;

Identifier
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;


Comment
 : ( '/*' .*? '*/' ) -> skip
 ;

Space
 : [ \t]+ -> skip
 ;

fragment Int
 : [1-9] Digit*
 | '0'
 ;
  
fragment Digit 
 : [0-9]
 ;
