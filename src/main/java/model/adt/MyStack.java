package model.adt;

import exception.ADTException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private final Stack<T> stack;

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty())
            throw new ADTException("Stack is empty");
        return stack.pop();
    }

    @Override
    public void push(T value) {
        stack.push(value);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // Top of stack should be first element in returned list
    @Override
    public List<T> getReversed() {
        List<T> result = new ArrayList<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            result.add(stack.get(i));
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (T element : getReversed()) {
            result += element.toString() + "\n";
        }
        return result;
    }
}
