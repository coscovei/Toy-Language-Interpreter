package model.adt;

import java.util.HashMap;
import java.util.Map;

import exception.ExpEvalException;


public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private final Map<K, V> map;

    public MyDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ExpEvalException {
        if (!isDefined(key)) {
            throw new ExpEvalException("Key is not defined in the dictionary: " + key.toString());
        }
        return map.get(key);
    }

    @Override
    public void update(K key, V value) {
        map.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result += entry.getKey().toString() + " -> " + entry.getValue().toString() + "\n";
        }
        return result;
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public MyIDictionary<K, V> shallowCopy() {
        MyDictionary<K, V> copy = new MyDictionary<>();
        copy.map.putAll(this.map);
        return copy;
    }

}