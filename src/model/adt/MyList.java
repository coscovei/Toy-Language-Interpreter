package model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T>  implements MyIList<T> {
    private final List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T value) {
        list.add(value);
    }

    @Override
    public String toString() {
        String result = "";
        for (T element : list) {
            result += element.toString() + "\n";
        }
        return result;
    }
}
