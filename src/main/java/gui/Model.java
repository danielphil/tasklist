package gui;

public class Model<T> extends Observable {
    private T value = null;

    public void set(T newValue) {
        value = newValue;
        sendNotification();
    }

    public T get() {
        return value;
    }
}
