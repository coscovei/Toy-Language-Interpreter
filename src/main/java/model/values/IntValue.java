package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue{
    private int val;
    public IntValue(int v){val=v;}
    public int getVal() {return val;}
    public String toString()
    {
        return String.valueOf(val);
    }
    public IType getType() { return new IntType();}
    public IValue deepCopy()
    {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IntValue that = (IntValue) obj;
        return this.val == that.val;
    }
}
