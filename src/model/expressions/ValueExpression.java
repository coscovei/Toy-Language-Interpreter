package model.expressions;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IType;
import model.values.IValue;

public class ValueExpression implements IExp {
    private IValue val;
    public ValueExpression(IValue val) {
        this.val = val;
    }

    @Override
    public IValue eval(MyIDictionary<String,IValue> tbl, MyIHeap<IValue> heap) throws MyException {
        return val;
    }

    @Override
    public IExp deepCopy() {
        return new ValueExpression(val.deepCopy());
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "val=" + val +
                '}';
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) {
        return val.getType();
    }

}
