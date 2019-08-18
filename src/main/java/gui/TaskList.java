package gui;

import task.ITaskSerialiser;
import task.Task;
import task.TaskDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TaskList extends JPanel {
    private final ArrayList<Task> tasks;
    private final JButton addNewButton;
    private final Supplier<ITaskSerialiser> serialiserFactory;
    private final NewTimePeriod timePeriod;

    public TaskList(TaskDatabase db, Supplier<ITaskSerialiser> serialiserFactory, NewTimePeriod timePeriod) {
        this.timePeriod = timePeriod;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.serialiserFactory = serialiserFactory;

        tasks = db.restore(serialiserFactory, timePeriod);
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

        TaskEditor editor = addItem(new Task(serialiserFactory, timePeriod));
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
