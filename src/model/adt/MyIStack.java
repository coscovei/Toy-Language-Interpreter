package model.adt;

import exception.ADTException;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T value);
    boolean isEmpty();
}
