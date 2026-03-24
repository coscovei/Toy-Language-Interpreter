package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType {
    private final IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType other) {
            return inner.equals(other.inner);
        }
        return false;
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }

    @Override
    public IValue defaultValue() {
        // address 0 will mean "null reference"
        return new RefValue(0, inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }
}
