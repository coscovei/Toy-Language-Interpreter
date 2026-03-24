package model.statements;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFile implements IStmt {
    private IExp exp;

    public OpenRFile(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue expEval = exp.eval(state.getSymTable(), state.getHeap());

        if (expEval.getType().equals(new StringType())) {
            StringValue value = (StringValue) expEval;
            if (state.getFileTable().isDefined(value)) {
                throw new MyException("file was already open.");
            } else {
                try {
                    state.getFileTable().put(value, new BufferedReader(new FileReader(value.getValue())));
                } catch (FileNotFoundException e) {
                    throw new MyException(e.getMessage());
                }
            }
        } else {
            throw new MyException("file name must be a string value.");
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (exp.typecheck(typeEnv).equals(new StringType())) return typeEnv;
        throw new MyException("openRFile: expression is not string");
    }

}
