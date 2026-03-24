package model.adt;
import exception.ExpEvalException;
import exception.MyException;
import java.util.Map;

public interface MyIDictionary<K, V> {
    V get(K key);
    void put(K key, V value);
    boolean isDefined(K key);
    V lookup(K key) throws ExpEvalException;
    void update(K key, V value);
    String toString();
    Map<K, V> getContent();
    void remove(K key);
    MyIDictionary<K, V> shallowCopy();

}