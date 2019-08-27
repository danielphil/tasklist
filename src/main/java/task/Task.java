package task;

import gui.TimePeriod;

public class Task {
    private String description = "";
    private boolean completed;
    private TimePeriod timePeriod;

    public Task(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Task() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}
