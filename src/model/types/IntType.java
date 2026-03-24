package model.types;
import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType{
    public boolean equals(Object another){
        if (another instanceof IntType)
            return true;
        else
            return false;
    }
    public IType deepCopy() {
        return new IntType();
    }
    public String toString() { return "int";}
    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }
}
