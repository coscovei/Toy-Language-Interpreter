package view;

import model.statements.*;
import model.expressions.*;
import model.types.*;
import model.values.*;
import model.expressions.ArithmeticalOperation;


public class Examples {

    public static IStmt example1() {

        IStmt stmt1 = new VarDeclStmt("v", new BoolType());
        IStmt stmt2 = new AssignmentStmt("v", new ValueExpression(new IntValue(2)));
        IStmt stmt3 = new PrintStmt(new VariableExpression("v"));
        return new CompStmt(stmt1, new CompStmt(stmt2, stmt3));
    }

    public static IStmt example2() {
        IStmt vA = new VarDeclStmt("a", new IntType());
        IStmt vB = new VarDeclStmt("b", new IntType());
        IExp expA = new ArithmeticalExpression(
                ArithmeticalOperation.PLUS,
                new ValueExpression(new IntValue(2)),
                new ArithmeticalExpression(
                        ArithmeticalOperation.STAR,
                        new ValueExpression(new IntValue(3)),
                        new ValueExpression(new IntValue(5))
                )
        );
        IStmt stA = new AssignmentStmt("a", expA);
        IExp expB = new ArithmeticalExpression(
                ArithmeticalOperation.PLUS,
                new VariableExpression("a"),
                new ValueExpression(new IntValue(1))
        );
        IStmt stB = new AssignmentStmt("b", expB);
        IStmt printB = new PrintStmt(new VariableExpression("b"));
        return new CompStmt(vA, new CompStmt(vB, new CompStmt(stA, new CompStmt(stB, printB))));
    }

    public static IStmt example3() {
        IStmt vA = new VarDeclStmt("a", new BoolType());
        IStmt vV = new VarDeclStmt("v", new IntType());

        IStmt stA = new AssignmentStmt("a", new ValueExpression(new BoolValue(true)));

        IStmt thenS = new AssignmentStmt("v", new ValueExpression(new IntValue(2)));
        IStmt elseS = new AssignmentStmt("v", new ValueExpression(new IntValue(3)));

        IStmt ifS = new IfStmt(new VariableExpression("a"), thenS, elseS);
        IStmt printV = new PrintStmt(new VariableExpression("v"));

        return new CompStmt(vA, new CompStmt(vV, new CompStmt(stA, new CompStmt(ifS, printV))));
    }

    public static IStmt example4_FileTest() {
        // string varf;
        IStmt stmt1 = new VarDeclStmt("varf", new StringType());

        // varf="test.in";
        IStmt stmt2 = new AssignmentStmt("varf", new ValueExpression(new StringValue("test.in")));

        // openRFile(varf);
        IStmt stmt3 = new OpenRFile(new VariableExpression("varf"));

        // int varc;
        IStmt stmt4 = new VarDeclStmt("varc", new IntType());

        // readFile(varf,varc);
        IStmt stmt5 = new ReadFile(new VariableExpression("varf"), "varc");

        // print(varc);
        IStmt stmt6 = new PrintStmt(new VariableExpression("varc"));

        // readFile(varf,varc); (Again)
        IStmt stmt7 = new ReadFile(new VariableExpression("varf"), "varc");

        // print(varc); (Again)
        IStmt stmt8 = new PrintStmt(new VariableExpression("varc"));

        // closeRFile(varf)
        IStmt stmt9 = new CloseRFile(new VariableExpression("varf"));

        return new CompStmt(stmt1, new CompStmt(stmt2, new CompStmt(stmt3, new CompStmt(stmt4,
                new CompStmt(stmt5, new CompStmt(stmt6, new CompStmt(stmt7, new CompStmt(stmt8, stmt9))))))));
    }

    public static IStmt example5_heapSimple() {
        // Ref int v; new(v,20); print(rH(v)); new(v,30); print(rH(v)+5)
        IStmt declV  = new VarDeclStmt("v", new RefType(new IntType()));
        IStmt alloc1 = new NewStmt("v", new ValueExpression(new IntValue(20)));
        IStmt print1 = new PrintStmt(new ReadHeapExpression(new VariableExpression("v")));
        IStmt alloc2 = new NewStmt("v", new ValueExpression(new IntValue(30)));
        IStmt print2 = new PrintStmt(
                new ArithmeticalExpression(
                        ArithmeticalOperation.PLUS,
                        new ReadHeapExpression(new VariableExpression("v")),
                        new ValueExpression(new IntValue(5))
                )
        );
        return new CompStmt(declV,
                new CompStmt(alloc1,
                        new CompStmt(print1,
                                new CompStmt(alloc2, print2))));
    }

    public static IStmt example6_nestedRefs() {
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))
        IStmt declV   = new VarDeclStmt("v", new RefType(new IntType()));
        IStmt allocV1 = new NewStmt("v", new ValueExpression(new IntValue(20)));
        IStmt declA   = new VarDeclStmt("a", new RefType(new RefType(new IntType())));
        IStmt allocA  = new NewStmt("a", new VariableExpression("v"));
        IStmt allocV2 = new NewStmt("v", new ValueExpression(new IntValue(30)));

        IExp inner = new ReadHeapExpression(new VariableExpression("a"));   // Ref int
        IExp outer = new ReadHeapExpression(inner);                         // int
        IStmt print = new PrintStmt(outer);

        return new CompStmt(declV,
                new CompStmt(allocV1,
                        new CompStmt(declA,
                                new CompStmt(allocA,
                                        new CompStmt(allocV2, print)))));
    }

    public static IStmt example7_while() {
        // int v; v=4; while (v>0) { print(v); v=v-1; } print(v)
        IStmt declV   = new VarDeclStmt("v", new IntType());
        IStmt assign4 = new AssignmentStmt("v", new ValueExpression(new IntValue(4)));

        IExp cond = new RelationalExpression(
                new VariableExpression("v"),
                new ValueExpression(new IntValue(0)),
                RelationalOperation.GREATER_THAN
        );

        IStmt printV = new PrintStmt(new VariableExpression("v"));
        IStmt decV   = new AssignmentStmt("v",
                new ArithmeticalExpression(
                        ArithmeticalOperation.MINUS,
                        new VariableExpression("v"),
                        new ValueExpression(new IntValue(1))
                ));

        IStmt body     = new CompStmt(printV, decV);
        IStmt whileCmd = new WhileStmt(cond, body);

        return new CompStmt(
                declV,
                new CompStmt(
                        assign4,
                        new CompStmt(whileCmd, new PrintStmt(new VariableExpression("v")))
                )
        );
    }

    /**
     * A5 fork demo:
     *
     * int v; Ref int a;
     * v=10; new(a,22);
     * fork( wH(a,30); v=32; print(v); print(rH(a)) );
     * print(v); print(rH(a));
     *
     * Expected behavior:
     * - v is NOT shared (child has its own symTable)
     * - heap IS shared, so wH in child affects what parent sees in rH(a)
     * - output order can vary because of concurrency
     */
    public static IStmt example8_forkSharedHeap() {
        // int v; Ref int a;
        IStmt declV = new VarDeclStmt("v", new IntType());
        IStmt declA = new VarDeclStmt("a", new RefType(new IntType()));

        // v = 10;
        IStmt assignV10 = new AssignmentStmt("v", new ValueExpression(new IntValue(10)));

        // new(a, 22);
        IStmt newA22 = new NewStmt("a", new ValueExpression(new IntValue(22)));

        // fork( wH(a,30); v=32; print(v); print(rH(a)) )
        IStmt childWrite = new WriteHeapStmt("a", new ValueExpression(new IntValue(30)));
        IStmt childAssign = new AssignmentStmt("v", new ValueExpression(new IntValue(32)));
        IStmt childPrintV = new PrintStmt(new VariableExpression("v"));
        IStmt childPrintRH = new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))); // <-- your class name

        IStmt childBody = new CompStmt(
                childWrite,
                new CompStmt(
                        childAssign,
                        new CompStmt(childPrintV, childPrintRH)
                )
        );

        IStmt forkStmt = new ForkStmt(childBody);

        // parent: print(v); print(rH(a)); nop
        IStmt parentPrintV = new PrintStmt(new VariableExpression("v"));
        IStmt parentPrintRH = new PrintStmt(new ReadHeapExpression(new VariableExpression("a")));
        IStmt parentTail = new CompStmt(parentPrintV,
                new CompStmt(parentPrintRH, new NopStmt())); // asta nou

        // full program
        return new CompStmt(
                declV,
                new CompStmt(
                        declA,
                        new CompStmt(
                                assignV10,
                                new CompStmt(
                                        newA22,
                                        new CompStmt(forkStmt, parentTail)
                                )
                        )
                )
        );
    }

}
