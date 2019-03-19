import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CalculatorVisitor extends calculatorBaseVisitor<Double> {
    private Env env;
    private Map<String, Function> functions;
    private static Break breakException = new Break();
    
    CalculatorVisitor(Env env, Map<String, Function> functions) {
        this.env = env;
        this.functions = functions;
    }

    // functionDecl
    @Override
    public Double visitFuncDecl(calculatorParser.FuncDeclContext ctx) {
        return null;
    }
    
    
    // '-' expr                           #unaryMinusExpr
    @Override
    public Double visitNegateExpr(calculatorParser.NegateExprContext ctx) {
    	Double v = this.visit(ctx.expr());
        return -1 * v;
    }

    // '!' expr                           #notExpr
    @Override
    public Double visitNotExpr(calculatorParser.NotExprContext ctx) {
    	Double v = this.visit(ctx.expr());
    	if(v >= -0.000000001 && v <= 0.000000001) {
    	    return 1.000000000;
        }
    	return (0.000000);
    }

    // expr '^' expr                #powerExpr
    @Override
    public Double visitPowerExpr(calculatorParser.PowerExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
    		return Math.pow(lhs, rhs);
    	}

    // expr op=( '*' | '/' | '%' ) expr         #multExpr
    @Override
    public Double visitMultExpr(calculatorParser.MultExprContext ctx) {
        switch (ctx.op.getType()) {
            case calculatorLexer.MUL:
                return multiply(ctx);
            case calculatorLexer.DIV:
                return divide(ctx);
            default:
                throw new RuntimeException("unknown operator: " + ctx.op.getType());
        }
    }

    @Override
    public Double visitAddExpr(calculatorParser.AddExprContext ctx) {
        switch (ctx.op.getType()) {
            case calculatorLexer.ADD:
                return add(ctx);
            case calculatorLexer.SUB:
                return subtract(ctx);
            default:
                throw new RuntimeException("unknown operator: " + ctx.op.getType());
        }
    }

    @Override
    public Double visitCompExpr(calculatorParser.CompExprContext ctx) {
        switch (ctx.op.getType()) {
            case calculatorLexer.LESSER:
                return lt(ctx);
            case calculatorLexer.LTEQUALS:
                return ltEq(ctx);
            case calculatorLexer.GREATER:
                return gt(ctx);
            case calculatorLexer.GTEQUALS:
                return gtEq(ctx);
            default:
                throw new RuntimeException("unknown operator: " + ctx.op.getType());
        }
    }

    @Override
    public Double visitEqExpr(calculatorParser.EqExprContext ctx) {
        switch (ctx.op.getType()) {
            case calculatorLexer.EQUALS:
                return eq(ctx);
            case calculatorLexer.NOTEQUALS:
                return nEq(ctx);
            default:
                throw new RuntimeException("unknown operator: " + ctx.op.getType());
        }
    }
    
    public Double multiply(calculatorParser.MultExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        return lhs * rhs;
    }
    
    private Double divide(calculatorParser.MultExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
        return lhs / rhs;
    }

    private Double add(calculatorParser.AddExprContext ctx) {
        Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        return lhs + rhs;
        }

    private Double subtract(calculatorParser.AddExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
    	return lhs - rhs;
    }

    private Double gtEq(calculatorParser.CompExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        if(lhs >= rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    private Double ltEq(calculatorParser.CompExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        if(lhs <= rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    private Double gt(calculatorParser.CompExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
    	if(lhs > rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    private Double lt(calculatorParser.CompExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        if(lhs < rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    private Double eq(calculatorParser.EqExprContext ctx) {
        Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        if(lhs == rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    private Double nEq(calculatorParser.EqExprContext ctx) {
        Double lhs = this.visit(ctx.expr(0));
        Double rhs = this.visit(ctx.expr(1));
        if(lhs != rhs)
        {
            return 1.;
        }
        else
        {
            return 0.;
        }
    }

    @Override
    public Double visitAndExpr(calculatorParser.AndExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
        if(lhs> 0. && rhs> 0.)
        {
            return 1.;
        }
        else{
            return 0.;
        }
    }

    @Override
    public Double visitOrExpr(calculatorParser.OrExprContext ctx) {
    	Double lhs = this.visit(ctx.expr(0));
    	Double rhs = this.visit(ctx.expr(1));
        if(lhs> 0. || rhs> 0.)
        {
            return 1.;
        }
        else
        {
            return 0.00000000000000000;
        }
    }

    @Override
    public Double visitExprExpr(calculatorParser.ExprExprContext ctx) {
	return this.visit(ctx.expr());
    }

	@Override
    public Double visitReadExpr(calculatorParser.ReadExprContext ctx) {
	Scanner reader = new Scanner(System.in);
        double n = reader.nextDouble();
        reader.close();
	return n;
    }

    @Override
    public Double visitPrintFuncCall(calculatorParser.PrintFuncCallContext ctx) {
	List<calculatorParser.ExprContext> params; 
            params = ctx.exprList().expr();
	for(calculatorParser.ExprContext ec : params) {
		System.out.print(this.visit(ec));
	}
	System.out.println();

            return null;
    }

    @Override
    public Double visitNumberExpr(calculatorParser.NumberExprContext ctx) {
        return Double.valueOf(ctx.getText());
    }

    @Override
    public Double visitFuncCallExpr(calculatorParser.FuncCallExprContext ctx) {
    	Double val = this.visit(ctx.funcCall());
    	return val;
    }

    @Override
    public Double visitExprPrint(calculatorParser.ExprPrintContext ctx) {
	if(this.visit(ctx.expr()) != null) {
       System.out.println(this.visit(ctx.expr()));
	}
        return null; 
    }


    @Override
    public Double visitIdentifierExpr(calculatorParser.IdentifierExprContext ctx) {
        String id = ctx.Identifier().getText();
        Double val = env.resolve(id);
        return val;
    }

    @Override
    public Double visitAssignment(calculatorParser.AssignmentContext ctx) {
        Double newVal = this.visit(ctx.expr());
        String id = ctx.Identifier().getText();        	
        env.assign(id, newVal);
        return newVal;
    }

    @Override
    public Double visitIdentifierFuncCall(calculatorParser.IdentifierFuncCallContext ctx) {
        List<calculatorParser.ExprContext> params; 
        if(ctx.exprList() != null)
        {
            params = ctx.exprList().expr();
        }
        else
        {
            params = new ArrayList<calculatorParser.ExprContext>();
        }

        String tId = ctx.Identifier().getText();
	if (params.size() == 1) {
        CalculatorVisitor calculatorVisitor = new CalculatorVisitor(env, functions);  
	Double paramT = calculatorVisitor.visit(params.get(0));
	

         if (tId.equals("s"))
        {
            return Math.sin(paramT);
        }
        else if (tId.equals("c"))
        {
            return Math.cos(paramT);
        }
        else if (tId.equals("a"))
        {
            return Math.atan(paramT);
        }
        else if (tId.equals("l"))
        {
            return Math.log(paramT);
        }
        else if (tId.equals("e"))
        {
            return Math.exp(paramT);
        }
	}
        String id = ctx.Identifier().getText() + params.size();
        Function function;      
        if ((function = functions.get(id)) != null) {
            return function.invoke(params, functions, env);
        }
        return null;
    }

    @Override
    public Double visitIfStatement(calculatorParser.IfStatementContext ctx) {
        if(this.visit(ctx.ifStat().expr()) > 0.) {
            return this.visit(ctx.ifStat().block());
        }

        if(ctx.elseStat() != null) {
            return this.visit(ctx.elseStat().block());
        }

        return null;
    }
    
    @Override
    public Double visitBlock(calculatorParser.BlockContext ctx) {
    		
    	env = new Env(env); 
        for (calculatorParser.StatementContext sx: ctx.statement()) {
            this.visit(sx);
        }
        calculatorParser.ExprContext ex;
        if ((ex = ctx.expr()) != null) {
        	breakException.value = this.visit(ex);
        	env = env.parent();
        	throw breakException;
        }
        env = env.parent();

        return null;
    }
    
    @Override
    public Double visitForStatement(calculatorParser.ForStatementContext ctx) {
	/*for (int i = 0; i < ctx.getChildCount(); i++) {
		System.out.println(ctx.getChild(i).getText() + " " + ctx.getChild(i).getClass().getName());
        }
	*/

        double start = this.visit(ctx.assignment(0));
	while(this.visit(ctx.expr()) > 0.) {
		this.visit(ctx.assignment(1));
		Double returnValue = this.visit(ctx.block());
		if(returnValue != null) {
                	return returnValue;
            	}
	}
        return null;
    }
    
    @Override
    public Double visitWhileStatement(calculatorParser.WhileStatementContext ctx) {
        while( this.visit(ctx.expr()) > 0. ) {
            Double returnValue = this.visit(ctx.block());
            if (returnValue != null) {
                return returnValue;
            }
        }
        return 0.0;
    }
    
}
