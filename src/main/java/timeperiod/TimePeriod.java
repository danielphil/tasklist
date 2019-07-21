package timeperiod;

import task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class TimePeriod {
    private final ITimePeriodSerialiser serialiser;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TimePeriod(Supplier<ITimePeriodSerialiser> serialiserFactory) {
        serialiser = serialiserFactory.get();
    }

    public void addTask(Task task) {
        tasks.add(task);
        serialiser.persist(this);
    }

    public Iterable<Task> getTasks() {
        return tasks;
    }

    protected abstract String getStringRepresentation();

    protected abstract String getTableName();

    protected void restore() {
        serialiser.restore(this);
    }
}
