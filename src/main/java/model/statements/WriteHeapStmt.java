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

public class WriteHeapStmt implements IStmt {
    private final String varName;
    private final IExp expression;

    public WriteHeapStmt(String varName, IExp expression) {
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
        if (!(varValue instanceof RefValue refValue)) {
            throw new MyException("Variable " + varName + " is not a RefValue.");
        }

        int address = refValue.getAddress();
        if (!heap.isDefined(address)) {
            throw new ExpEvalException("Address " + address + " is not in the heap.");
        }

        IValue evaluated = expression.eval(symTable, heap);
        if (!evaluated.getType().equals(refValue.getLocationType())) {
            throw new ExpEvalException("Type mismatch in wH(" + varName + ", expression).");
        }

        heap.update(address, evaluated);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeapStmt(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(varName);
        if (!(typeVar instanceof RefType refT))
            throw new MyException("wH: variable is not a RefType");

        IType typeExp = expression.typecheck(typeEnv);
        if (refT.getInner().equals(typeExp)) return typeEnv;

        throw new MyException("wH: expression type doesn't match ref inner type");
    }

}
