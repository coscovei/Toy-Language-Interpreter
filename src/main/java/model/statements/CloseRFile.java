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
import java.io.IOException;

public class CloseRFile implements IStmt {
    private final IExp exp;

    public CloseRFile(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTbl = state.getFileTable();

        // Evaluate exp to a string [cite: 493]
        IValue expValue = exp.eval(symTbl, state.getHeap());
        if (!expValue.getType().equals(new StringType())) {
            throw new MyException("File path expression is not a string type.");
        }

        // Get the BufferedReader from the FileTable [cite: 495]
        StringValue filePath = (StringValue) expValue;
        if (!fileTbl.isDefined(filePath)) {
            throw new MyException("File '" + filePath + "' is not open in the File Table.");
        }
        BufferedReader reader = fileTbl.lookup(filePath);

        try {
            // Call the close method [cite: 497]
            reader.close();
        } catch (IOException e) {
            throw new MyException("Error closing file '" + filePath + "': " + e.getMessage());
        }

        // Delete the entry from the FileTable [cite: 498]
        fileTbl.remove(filePath);

        return null; // Controller handles the pop
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (exp.typecheck(typeEnv).equals(new StringType())) return typeEnv;
        throw new MyException("closeRFile: expression is not string");
    }

}