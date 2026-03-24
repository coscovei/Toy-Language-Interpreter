package model.statements;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    private final IExp exp;
    private final String var_name;

    public ReadFile(IExp exp, String var_name) {
        this.exp = exp;
        this.var_name = var_name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTbl = state.getFileTable();

        // Check whether var_name is defined in SymTable and its type is int [cite: 481]
        if (!symTbl.isDefined(var_name)) {
            throw new MyException("Variable '" + var_name + "' is not defined in the Symbol Table.");
        }
        if (!symTbl.lookup(var_name).getType().equals(new IntType())) {
            throw new MyException("Variable '" + var_name + "' is not of type int.");
        }

        // Evaluate exp to a string value [cite: 483]
        IValue expValue = exp.eval(symTbl, state.getHeap());
        if (!expValue.getType().equals(new StringType())) {
            throw new MyException("File path expression is not a string type.");
        }

        // Get the BufferedReader from the FileTable [cite: 485]
        StringValue filePath = (StringValue) expValue;
        if (!fileTbl.isDefined(filePath)) {
            throw new MyException("File '" + filePath + "' is not open in the File Table.");
        }
        BufferedReader reader = fileTbl.lookup(filePath);

        try {
            // Reads a line from the file [cite: 487]
            String line = reader.readLine();
            int readValue;

            if (line == null) {
                // If line is null, creates a zero int value [cite: 488]
                readValue = 0;
            } else {
                // Otherwise translate the returned String into an int value [cite: 488]
                readValue = Integer.parseInt(line);
            }

            // Update SymTable with the new value [cite: 489]
            symTbl.update(var_name, new IntValue(readValue));

        } catch (IOException e) {
            throw new MyException("Error reading from file '" + filePath + "': " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MyException("File '" + filePath + "' contains non-integer data.");
        }

        return null; // Controller handles the pop
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(exp.deepCopy(), var_name);
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + var_name + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (!exp.typecheck(typeEnv).equals(new StringType()))
            throw new MyException("readFile: filename expression is not string");
        if (!typeEnv.lookup(var_name).equals(new IntType()))
            throw new MyException("readFile: target variable is not int");
        return typeEnv;
    }

}