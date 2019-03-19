import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Function {

    private List<TerminalNode> params;
    private ParseTree block;

    Function(List<TerminalNode> params, ParseTree block) {
        this.params = params;
        this.block = block;
    }
    
    public Double invoke(List<calculatorParser.ExprContext> params, Map<String, Function> functions, Env env) {
        if (params.size() != this.params.size())
            throw new RuntimeException("Invalid Number of Params");
        Env envNext = new Env(null);

        CalculatorVisitor calculatorVisitor = new CalculatorVisitor(env, functions); 
        for (int i = 0; i < this.params.size(); i++) {
            Double value = calculatorVisitor.visit(params.get(i));
            envNext.assignParam(this.params.get(i).getText(), value);
        }
        CalculatorVisitor evalVistorNext = new CalculatorVisitor(envNext,functions);
        
        Double ret = null;
        try {
        	evalVistorNext.visit(this.block);
        } catch (Break breakException) {
        	ret = breakException.value;
        }
        return ret;
    }
}
