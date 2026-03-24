package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;
import model.adt.MyIList;
import exception.MyException;
import model.adt.MyIHeap;


public class PrintStmt implements IStmt {
    private final IExp exp;

    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<IValue> out = state.getOut();
        IValue val = exp.eval(state.getSymTable(), state.getHeap());
        out.add(val);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "Print(" + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

}
