package model.expressions;

import exception.ExpEvalException;
import exception.MyException;
import model.adt.MyIDictionary;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;
import model.adt.MyIHeap;


public class LogicalExpression implements IExp {
    private IExp left;
    private IExp right;
    private LogicalOperation op;

    public LogicalExpression(IExp left, IExp right, LogicalOperation op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap<IValue> heap) throws MyException {
        IValue exp1 = left.eval(table, heap);
        if (!exp1.getType().equals(new BoolType())) {
            throw new ExpEvalException("First operand is not bool.");
        }

        IValue exp2 = right.eval(table, heap);
        if (!exp2.getType().equals(new BoolType())) {
            throw new ExpEvalException("Second operand is not bool.");
        }

        boolean b1 = ((BoolValue) exp1).getVal();
        boolean b2 = ((BoolValue) exp2).getVal();
        boolean result;

        switch (op) {
            case AND -> result = b1 && b2;
            case OR  -> result = b1 || b2;
            default  -> throw new ExpEvalException("Unknown logical operator.");
        }

        return new BoolValue(result);
    }

    @Override
    public IExp deepCopy() {
        return new LogicalExpression(left.deepCopy(), right.deepCopy(), op);
    }

    @Override
    public String toString() {
        return "LogicalExpression{" +
                "left=" + left +
                ", right=" + right +
                ", op=" + op +
                '}';
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType t1 = left.typecheck(typeEnv);
        IType t2 = right.typecheck(typeEnv);
        if (t1.equals(new BoolType())) {
            if (t2.equals(new BoolType())) return new BoolType();
            throw new MyException("second operand is not a boolean");
        }
        throw new MyException("first operand is not a boolean");
    }

}
