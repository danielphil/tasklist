package gui;

import javax.swing.*;

public class TaskList extends JPanel {
    private final TaskListModel model;

    public TaskList(TaskListModel model) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.model = model;

        for (TaskModel task : model.tasks()) {
            add(new TaskEditor(task));
        }
    }
}
