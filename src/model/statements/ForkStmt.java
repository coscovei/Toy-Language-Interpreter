package model.statements;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.adt.MyIList;
import model.adt.MyIStack;
import model.adt.MyStack;
import model.types.IType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class ForkStmt implements IStmt {
    private final IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // new execution stack (child thread)
        MyIStack<IStmt> newStack = new MyStack<>();

        // clone symTable (NOT shared)
        MyIDictionary<String, IValue> newSymTable = state.getSymTable().shallowCopy();

        // shared structures
        MyIList<IValue> out = state.getOut();
        MyIHeap<IValue> heap = state.getHeap();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        // IMPORTANT: our PrgState constructor pushes 'prg' onto the given stack,
        // so we pass an empty newStack (do NOT push stmt here).
        return new PrgState(
                fileTable,
                newStack,
                newSymTable,
                out,
                heap,
                stmt
        );
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
