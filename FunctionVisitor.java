
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class FunctionVisitor extends calculatorBaseVisitor<Double> {

    private Map<String, Function> functions;
    
    FunctionVisitor(Map<String, Function> functions) {
        this.functions = functions;
    }
    
    @Override
    public Double visitFuncDecl(calculatorParser.FuncDeclContext context) {
        List<TerminalNode> params;
        if(context.idList() != null)
            params = context.idList().Identifier();
        else 
            params = new ArrayList<TerminalNode>(); 
        ParseTree block = context.block();
        String id = context.Identifier().getText() + params.size();
        functions.put(id, new Function(params, block));
        return null;
    }
}
