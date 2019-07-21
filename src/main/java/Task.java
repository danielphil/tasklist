public class Task {
    private String description = "";
    private boolean completed = false;
    private Long id = null;
    private ITimePeriod timePeriod = null;

    public Task() {
    }

    public Task(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    public Task(String description, boolean completed, long id) {
        this(description, completed);
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean completed() {
        return this.completed;
    }

    public long getId() {
        if (id == null) {
            throw new RuntimeException("ID is not set");
        }
        return id.longValue();
    }

    public boolean hasId() {
        return id != null;
    }

    void setCompleted(boolean isCompleted) {
        this.completed = isCompleted;
    }

    public void associateWithTimePeriod(ITimePeriod period) {
        this.timePeriod = period;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }

    public ITimePeriod getTimePeriod() {
        return this.timePeriod;
    }
}
