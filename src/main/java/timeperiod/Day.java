package timeperiod;

import task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Day {
    private final LocalDate day;
    private final IDaySerialiser serialiser;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public Day(Supplier<IDaySerialiser> serialiserFactory, LocalDate day) {
        serialiser = serialiserFactory.get();
        this.day = day;
        serialiser.restore(this);
    }

    public LocalDate getDay() {
        return day;
    }

    public void addTask(Task task) {
        tasks.add(task);
        serialiser.persist(this);
    }

    public Iterable<Task> getTasks() {
        return tasks;
    }
}
