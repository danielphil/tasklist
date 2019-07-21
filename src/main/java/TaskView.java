import java.util.List;

public class TaskView {
    private final ITimePeriod time_period;
    private final Database database;
    private List<Task> tasks;

    public TaskView(ITimePeriod time_period, Database db) {
        this.time_period = time_period;
        this.database = db;
        tasks = db.tasksForTimePeriod(time_period);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void AddNewTask(Task task) {
        task.associateWithTimePeriod(time_period);
        database.updateTask(task);
    }
}
