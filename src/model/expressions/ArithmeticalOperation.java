package model.expressions;

public enum ArithmeticalOperation {
    PLUS("+", 1),
    MINUS("-", 2),
    STAR("*", 3),
    DIVIDE("/", 4);

    private final String symbol;
    private final int opCode;

    ArithmeticalOperation(String symbol, int opCode) {
        this.symbol = symbol;
        this.opCode = opCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getOpCode() {
        return opCode;
    }
}
