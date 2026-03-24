package model.adt;

import exception.ADTException;
import java.util.Map;

public interface MyIHeap<V> {
    int allocate(V value);                // returns new address
    V get(int address) throws ADTException;
    void update(int address, V value) throws ADTException;
    boolean isDefined(int address);
    Map<Integer, V> getContent();
    void setContent(Map<Integer, V> newHeap);
}
