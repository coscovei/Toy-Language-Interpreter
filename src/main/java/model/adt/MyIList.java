package model.adt;

import java.util.List;

public interface MyIList<T> {
    void add(T value);

    // A7 GUI needs to display Out as a list
    List<T> getContent();
}
