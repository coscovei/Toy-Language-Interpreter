package model.expressions;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IType;
import model.values.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue> table, MyIHeap<IValue> heap) throws MyException;
    IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException;
    IExp deepCopy();
}
