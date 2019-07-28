package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskListModel extends Observable {
    private ArrayList<TaskModel> tasks = new ArrayList<>();

    public TaskListModel() {
    }

    public List<TaskModel> tasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addItem(TaskModel task) {
        tasks.add(task);
        sendNotification();
    }
}
