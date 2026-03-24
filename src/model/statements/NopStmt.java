package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import exception.MyException;
import model.types.IType;

public class NopStmt implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public String toString() {
        return "nop";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) {
        return typeEnv;
    }

}
