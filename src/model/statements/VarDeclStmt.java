package model.statements;

import model.PrgState;
import model.adt.MyIStack;
import model.types.IType;
import model.values.IValue;
import model.adt.MyIDictionary;
import exception.MyException;

public class VarDeclStmt implements IStmt {
    private final String name;
    private final IType typ;

    public VarDeclStmt(String name, IType typ) {
        this.name = name;
        this.typ = typ;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIStack<IStmt> stk = state.getExeStack();

        if (symTbl.isDefined(name)) {
            throw new MyException("variable " + name + " is already declared");
        }

        IValue defaultValue = typ.defaultValue();
        symTbl.update(name, defaultValue);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name,typ.deepCopy());
    }

    @Override
    public String toString() {
        return typ.toString() + " " + name;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) {
        typeEnv.put(name, typ);
        return typeEnv;
    }

}
