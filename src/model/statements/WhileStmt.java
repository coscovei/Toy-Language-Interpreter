package model.statements;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStmt implements IStmt {
    private final IExp expression;
    private final IStmt statement;

    public WhileStmt(IExp expression, IStmt statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue cond = expression.eval(state.getSymTable(), state.getHeap());
        if (!cond.getType().equals(new BoolType())) {
            throw new MyException("While condition is not a boolean.");
        }

        BoolValue bValue = (BoolValue) cond;
        if (bValue.getVal()) {
            // while(exp) stmt == stmt ; while(exp) stmt
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + expression.toString() + ") " + statement.toString();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = expression.typecheck(typeEnv);
        if (!typeExp.equals(new BoolType()))
            throw new MyException("WHILE: condition is not bool");

        statement.typecheck(typeEnv.shallowCopy());
        return typeEnv;
    }

}
