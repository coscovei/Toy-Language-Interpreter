package model;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.adt.MyIList;
import model.adt.MyIStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private static int lastId = 0;
    private final int id;

    private final MyIStack<IStmt> exeStack;
    private final MyIDictionary<String, IValue> symTable;
    private final MyIList<IValue> out;
    private final IStmt originalProgram;
    private final MyIDictionary<StringValue, BufferedReader> fileTable;
    private final MyIHeap<IValue> heap;

    private static synchronized int newId() {
        lastId++;
        return lastId;
    }

    public PrgState(MyIDictionary<StringValue, BufferedReader> filetbl,
                    MyIStack<IStmt> stk,
                    MyIDictionary<String, IValue> symtbl,
                    MyIList<IValue> ot,
                    MyIHeap<IValue> heap,
                    IStmt prg) {

        this.id = newId();
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = prg.deepCopy();
        this.fileTable = filetbl;
        this.heap = heap;

        stk.push(prg);
    }

    public int getId() {
        return id;
    }

    public MyIHeap<IValue> getHeap() {
        return heap;
    }

    public MyIList<IValue> getOut() {
        return out;
    }

    public MyIDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    // A5: perform one statement step; returns a new PrgState only for fork, otherwise null
    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty()) {
            throw new MyException("PrgState stack is empty");
        }
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "\n" +
                "================================================\n" +
                "Id=" + id + "\n" +
                "ExeStack:\n" +
                exeStack.toString() + "\n" +
                "SymTable:\n" +
                symTable.toString() + "\n" +
                "Out:\n" +
                out.toString() + "\n" +
                "Heap:\n" +
                heap.toString() + "\n" +
                "FileTable:\n" +
                fileTable.toString() + "\n" +
                "================================================\n\n";
    }
}
