import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

//https://github.com/antlr/antlr4/blob/master/doc/tree-matching.md

public class calculator {
    public static void main(String[] args) {
        String filename;
        if (args.length == 1) {
            filename = args[0];
        }
        else {
            System.out.println("Please provide path to bc file");
            return;
        }
        try {
            calculatorLexer lexer = new calculatorLexer(CharStreams.fromFileName(filename));
            calculatorParser parser = new calculatorParser(new CommonTokenStream(lexer));
            // USe antlr parse tree
            parser.setBuildParseTree(true);
            ParseTree tree = parser.parse();
           
            // Create enviroment + function table
            Env env = new Env();
            Map<String, Function> functions = new HashMap<>();
            FunctionVisitor functionVisitor = new FunctionVisitor(functions);
            functionVisitor.visit(tree);
            CalculatorVisitor visitor = new CalculatorVisitor(env, functions);
            visitor.visit(tree);
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
}
