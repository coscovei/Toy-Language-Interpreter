package model.expressions;

import exception.ExpEvalException;
import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExpression implements IExp {
    private final IExp exp;

    public ReadHeapExpression(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<IValue> heap) throws MyException {
        IValue v = exp.eval(tbl, heap);
        if (!(v instanceof RefValue refValue)) {
            throw new ExpEvalException("rH argument is not a RefValue.");
        }
        int address = refValue.getAddress();
        if (!heap.isDefined(address)) {
            throw new ExpEvalException("Address " + address + " not found in heap.");
        }
        return heap.get(address);
    }

    @Override
    public IExp deepCopy() {
        return new ReadHeapExpression(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ = exp.typecheck(typeEnv);
        if (typ instanceof RefType refT) return refT.getInner();
        throw new MyException("the rH argument is not a Ref Type");
    }

}
