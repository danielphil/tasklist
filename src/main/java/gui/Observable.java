package gui;

import java.util.ArrayList;

public class Observable {
    private ArrayList<Runnable> callbacks = new ArrayList<>();

    public void observe(Runnable callback) {
        callbacks.add(callback);
    }

    protected void sendNotification() {
        for (Runnable r : callbacks) {
            r.run();
        }
    }
}
