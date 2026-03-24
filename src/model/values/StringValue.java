package model.values;

import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import java.util.Objects;

public class StringValue implements IValue {
    public String value;
    public StringValue(String value) {
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

    public String toString(){
        return value;
    }
    public IValue deepCopy() { return new StringValue(value); }
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StringValue that = (StringValue) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
