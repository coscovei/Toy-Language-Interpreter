package model.expressions;

import model.adt.MyIDictionary;
import model.types.IType;
import model.types.IntType;
import model.types.BoolType;
import model.values.IValue;
import model.values.IntValue;
import model.values.BoolValue;
import exception.MyException;
import exception.ExpEvalException;
import model.adt.MyIHeap;

public class RelationalExpression implements IExp {
    private final IExp e1;
    private final IExp e2;
    private final RelationalOperation op;

    public RelationalExpression(IExp e1, IExp e2, RelationalOperation op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<IValue> heap) throws MyException {
        IValue v1 = e1.eval(tbl, heap);
        IValue v2 = e2.eval(tbl, heap);

        if (!v1.getType().equals(new IntType()) || !v2.getType().equals(new IntType())) {
            throw new ExpEvalException("Relational operands must be integers.");
        }

        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();
        boolean result;

        switch (op) {
            case LESS_THAN          -> result = n1 < n2;
            case LESS_THAN_EQUAL    -> result = n1 <= n2;
            case EQUAL              -> result = n1 == n2;
            case NOT_EQUAL          -> result = n1 != n2;
            case GREATER_THAN       -> result = n1 > n2;
            case GREATER_THAN_EQUAL -> result = n1 >= n2;
            default -> throw new ExpEvalException("Unknown relational operator.");
        }

        return new BoolValue(result);
    }

    @Override
    public IExp deepCopy() {
        return new RelationalExpression(e1.deepCopy(), e2.deepCopy(), op);
    }

    @Override
    public String toString() {
        return "(" + e1.toString() + op.getSymbol() + e2.toString() + ")";
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType t1 = e1.typecheck(typeEnv);
        IType t2 = e2.typecheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) return new BoolType();
            throw new MyException("second operand is not an integer");
        }
        throw new MyException("first operand is not an integer");
    }

}