package model.expressions;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IType;
import model.values.IValue;

public class VariableExpression implements IExp {
    private String id;
    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<IValue> heap) throws MyException {
        return tbl.lookup(id);
    }

    @Override
    public IExp deepCopy() {
        return new VariableExpression(id);
    }

    @Override
    public String toString() {
        return "VariableExpression{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }

}
