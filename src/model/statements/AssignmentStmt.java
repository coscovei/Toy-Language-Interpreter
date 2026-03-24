package model.statements;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.adt.MyIStack;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;

public class AssignmentStmt implements IStmt {
    private final String id;
    private final IExp exp;

    public AssignmentStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap<IValue> heap = state.getHeap();

        if (!symTable.isDefined(id)) {
            throw new MyException("Variable " + id + " is not declared.");
        }

        IValue value = exp.eval(symTable, heap);
        IType typeId = symTable.get(id).getType();

        if (!value.getType().equals(typeId)) {
            throw new MyException("Type mismatch: variable " + id + " is " + typeId +
                    " but expression has type " + value.getType());
        }

        symTable.update(id, value);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignmentStmt(id, exp.deepCopy());
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(id);
        IType typeExp = exp.typecheck(typeEnv);
        if (typeVar.equals(typeExp)) return typeEnv;
        throw new MyException("Assignment: RHS and LHS have different types");
    }
}
