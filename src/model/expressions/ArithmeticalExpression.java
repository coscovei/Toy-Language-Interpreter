package model.expressions;

import model.adt.MyIDictionary;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;
import exception.MyException;
import exception.ExpEvalException;
import model.adt.MyIHeap;

public class ArithmeticalExpression implements IExp {
    private final IExp e1;
    private final IExp e2;
    private final ArithmeticalOperation op;

    public ArithmeticalExpression(ArithmeticalOperation op, IExp e1, IExp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<IValue> heap) throws MyException {
        IValue v1 = e1.eval(tbl, heap);
        if (!v1.getType().equals(new IntType())) {
            throw new ExpEvalException("First operand is not int.");
        }

        IValue v2 = e2.eval(tbl, heap);
        if (!v2.getType().equals(new IntType())) {
            throw new ExpEvalException("Second operand is not int.");
        }

        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();

        return switch (op) {
            case PLUS -> new IntValue(n1 + n2);
            case MINUS -> new IntValue(n1 - n2);
            case STAR -> new IntValue(n1 * n2);
            case DIVIDE -> {
                if (n2 == 0) throw new ExpEvalException("Division by zero.");
                yield new IntValue(n1 / n2);
            }
            default -> throw new ExpEvalException("Invalid arithmetic operator.");
        };
    }

    @Override
    public IExp deepCopy() {
        return new ArithmeticalExpression(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public String toString() {
        return e1.toString() + " " + op.getSymbol() + " " + e2.toString();
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType t1 = e1.typecheck(typeEnv);
        IType t2 = e2.typecheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) return new IntType();
            throw new MyException("second operand is not an integer");
        }
        throw new MyException("first operand is not an integer");
    }

}