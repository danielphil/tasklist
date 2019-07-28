package gui;

public class TaskModel extends Observable {
    private boolean completed;
    private String description;

    public TaskModel(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        sendNotification();
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        sendNotification();
    }
}
