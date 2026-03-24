package model.expressions;

public enum RelationalOperation {
    LESS_THAN("<"),
    LESS_THAN_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    GREATER_THAN_EQUAL(">=");

    private final String symbol;

    RelationalOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}