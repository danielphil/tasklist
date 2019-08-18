package task;

import gui.TimePeriod;

import java.util.function.Supplier;

public class Task {
    private String description = "";
    private boolean completed;
    private TimePeriod timePeriod;

    // TODO: make this private. This is just for quick testing at the moment
    public final ITaskSerialiser serialiser;

    public Task(Supplier<ITaskSerialiser> serialiserFactory, TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
        serialiser = serialiserFactory.get();
        serialiser.persist(this);
    }

    public Task(Supplier<ITaskSerialiser> serialiserFactory, long id) {
        serialiser = serialiserFactory.get();
        serialiser.restore(id, this);
    }

    public void setDescription(String description) {
        this.description = description;
        serialiser.persist(this);
    }

    public String getDescription() {
        return description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        serialiser.persist(this);
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
        serialiser.persist(this);
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}
