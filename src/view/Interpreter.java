package view;

import controller.Controller;
import exception.MyException;
import model.PrgState;
import model.adt.*;
import model.statements.IStmt;
import model.types.IType;

public class Interpreter {

    // Helper to create a fully initialized PrgState
    private static PrgState createProgramState(IStmt program) throws MyException {
        try {
            MyIDictionary<String, IType> typeEnv = new MyDictionary<>();
            program.typecheck(typeEnv);
        } catch (MyException e) {
            throw new MyException("Typecheck failed: " + e.getMessage());
        }

        return new PrgState(
                new MyDictionary<>(), // FileTable
                new MyStack<>(),      // ExeStack
                new MyDictionary<>(), // SymTable
                new MyList<>(),       // Out
                new MyHeap<>(),       // Heap
                program               // Original program
        );
    }

    public static void main(String[] args) {

        TextMenu menu = new TextMenu();

        // Controllers for all examples
        Controller ctrl1 = null;
        Controller ctrl2 = null;
        Controller ctrl3 = null;
        Controller ctrl4 = null;
        Controller ctrl5 = null;
        Controller ctrl6 = null;
        Controller ctrl7 = null;
        Controller ctrl8 = null; // NEW (fork)

        String logFilePath = "log.txt";

        // ---------- Example 1 ----------
        IStmt ex1 = Examples.example1();
        try {
            PrgState prg1 = createProgramState(ex1);
            ctrl1 = new Controller(prg1, logFilePath);
            menu.addCommand(new RunExample("1", "Example 1", ctrl1, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 1: " + e.getMessage());
        }

        // ---------- Example 2 ----------
        IStmt ex2 = Examples.example2();
        try {
            PrgState prg2 = createProgramState(ex2);
            ctrl2 = new Controller(prg2, logFilePath);
            menu.addCommand(new RunExample("2", "Example 2", ctrl2, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 2: " + e.getMessage());
        }

        // ---------- Example 3 ----------
        IStmt ex3 = Examples.example3();
        try {
            PrgState prg3 = createProgramState(ex3);
            ctrl3 = new Controller(prg3, logFilePath);
            menu.addCommand(new RunExample("3", "Example 3", ctrl3, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 3: " + e.getMessage());
        }

        // ---------- Example 4: File test ----------
        IStmt ex4 = Examples.example4_FileTest();
        try {
            PrgState prg4 = createProgramState(ex4);
            ctrl4 = new Controller(prg4, logFilePath);
            menu.addCommand(new RunExample("4", "Example 4 - File example", ctrl4, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 4 (FileTest): " + e.getMessage());
        }

        // ---------- Example 5: Simple heap usage ----------
        IStmt ex5 = Examples.example5_heapSimple();
        try {
            PrgState prg5 = createProgramState(ex5);
            ctrl5 = new Controller(prg5, logFilePath);
            menu.addCommand(new RunExample("5", "Example 5 - Heap simple", ctrl5, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 5 (Heap simple): " + e.getMessage());
        }

        // ---------- Example 6: Nested references + GC ----------
        IStmt ex6 = Examples.example6_nestedRefs();
        try {
            PrgState prg6 = createProgramState(ex6);
            ctrl6 = new Controller(prg6, logFilePath);
            menu.addCommand(new RunExample("6", "Example 6 - Nested references & GC", ctrl6, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 6 (Nested refs): " + e.getMessage());
        }

        // ---------- Example 7: While loop ----------
        IStmt ex7 = Examples.example7_while();
        try {
            PrgState prg7 = createProgramState(ex7);
            ctrl7 = new Controller(prg7, logFilePath);
            menu.addCommand(new RunExample("7", "Example 7 - While loop", ctrl7, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 7 (While): " + e.getMessage());
        }

        // ---------- Example 8: Fork (A5) ----------
        IStmt ex8 = Examples.example8_forkSharedHeap();
        try {
            PrgState prg8 = createProgramState(ex8);
            ctrl8 = new Controller(prg8, logFilePath);
            menu.addCommand(new RunExample("8", "Example 8 - Fork (shared heap)", ctrl8, logFilePath));
        } catch (Exception e) {
            System.err.println("Error setting up Example 8 (Fork): " + e.getMessage());
        }

        // ---------- Menu ----------
        menu.addCommand(new ExitCommand("0", "Exit application"));
        menu.show();
    }
}
