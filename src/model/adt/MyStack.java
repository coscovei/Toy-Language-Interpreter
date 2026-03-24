package model.adt;

import exception.ADTException;

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

    @Override
    public String toString() {
        String result = "";
        Stack<T> auxStack = new Stack<>();

        // 1. Pop all elements to auxStack (reverses order)
        while (!stack.isEmpty()) {
            auxStack.push(stack.pop());
        }

        // 2. Pop from auxStack, build string, and push back to restore
        while (!auxStack.isEmpty()) {
            T element = auxStack.pop();
            // Add element and a newline
            result += element.toString() + "\n";
            // Restore the original stack
            stack.push(element);
        }

        // Return the multi-line string (or an empty string if stack was empty)
        return result;
    }
}


