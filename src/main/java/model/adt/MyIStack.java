package model.adt;

import exception.ADTException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T value);
    boolean isEmpty();

    // A7 GUI needs to display stack top -> bottom
    List<T> getReversed();
}
