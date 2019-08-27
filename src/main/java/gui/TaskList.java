package gui;

import task.SqlLiteTask;
import task.Task;
import task.TaskDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TaskList extends JPanel {
    private final ArrayList<Task> tasks;
    private final JButton addNewButton;
    private final TimePeriod timePeriod;
    private final TaskDatabase database;

    public TaskList(TaskDatabase db, TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
        this.database = db;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tasks = db.restore(timePeriod);
        for (Task task : tasks) {
            addItem(task);
        }

        addNewButton = new JButton("+");
        addNewButton.addActionListener((ActionEvent a) -> addNewItem());
        add(addNewButton);
    }

    private void onAddComplete(TaskEditor editor) {
        editor.setOnEditCompleteHandler(null);
        add(addNewButton);
        this.revalidate();
    }

    private void onTaskDeleted(TaskEditor editor) {
        editor.setOnTaskDeleted(null);
        remove(editor);
        this.revalidate();
        this.getRootPane().repaint();
    }

    private void addNewItem() {
        remove(addNewButton);

        TaskEditor editor = addItem(new SqlLiteTask(database, timePeriod));
        editor.startEditing();
        editor.setOnEditCompleteHandler(this::onAddComplete);

        this.revalidate();
    }

    private TaskEditor addItem(Task task) {
        TaskEditor editor = new TaskEditor(task);
        editor.setOnTaskDeleted(this::onTaskDeleted);
        add(editor);
        return editor;
    }
}
