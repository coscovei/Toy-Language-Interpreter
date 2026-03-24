package model.adt;

import exception.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyHeap<V> implements MyIHeap<V> {
    private Map<Integer, V> heap;
    private int nextFreeAddress;

    public MyHeap() {
        this.heap = new HashMap<>();
        this.nextFreeAddress = 1;
    }

    private int getNewAddress() {
        while (heap.containsKey(nextFreeAddress)) {
            nextFreeAddress++;
        }
        return nextFreeAddress;
    }

    @Override
    public int allocate(V value) {
        int address = getNewAddress();
        heap.put(address, value);
        return address;
    }

    @Override
    public V get(int address) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("Invalid heap address " + address);
        }
        return heap.get(address);
    }

    @Override
    public void update(int address, V value) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("Invalid heap address " + address);
        }
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, V> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, V> newHeap) {
        this.heap = newHeap;
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
