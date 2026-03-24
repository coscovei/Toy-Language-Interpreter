package model.statements;

import exception.ExpEvalException;
import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.IExp;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class NewStmt implements IStmt {
    private final String varName;
    private final IExp expression;

    public NewStmt(String varName, IExp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap<IValue> heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new MyException("Variable " + varName + " is not declared.");
        }

        IValue varValue = symTable.get(varName);
        if (!(varValue.getType() instanceof RefType refType)) {
            throw new MyException("Variable " + varName + " is not of RefType.");
        }

        IValue evaluated = expression.eval(symTable, heap);
        IType locationType = refType.getInner();

        if (!evaluated.getType().equals(locationType)) {
            throw new ExpEvalException("Type mismatch: " + varName + " refers to " + locationType +
                    " but expression is " + evaluated.getType());
        }

        int address = heap.allocate(evaluated);
        symTable.update(varName, new RefValue(address, locationType));

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(varName);
        IType typeExp = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) return typeEnv;
        throw new MyException("NEW: RHS and LHS have different types");
    }

}
