package timeperiod;

import task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Week {
    private final int weekNumber;
    private final int year;
    private final IWeekSerialiser serialiser;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public Week(Supplier<IWeekSerialiser> serialiserFactory, int weekNumber, int year) {
        serialiser = serialiserFactory.get();
        this.weekNumber = weekNumber;
        this.year = year;
        serialiser.restore(this);
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getYear() { return year; }

    public void addTask(Task task) {
        tasks.add(task);
        serialiser.persist(this);
    }

    public Iterable<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return String.format("%d2-%d4", weekNumber, year);
    }
}
