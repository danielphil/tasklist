package gui;

import task.ITaskSerialiser;
import task.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TaskList extends JPanel {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final JButton addNewButton;
    private final Supplier<ITaskSerialiser> serialiserFactory;
    private final NewTimePeriod timePeriod;

    public TaskList(Supplier<ITaskSerialiser> serialiserFactory, NewTimePeriod timePeriod) {
        this.timePeriod = timePeriod;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.serialiserFactory = serialiserFactory;

        /*
        for (TaskModel task : model.tasks()) {
            TaskEditor editor = new TaskEditor(task);
            editor.setOnTaskDeleted(this::onTaskDeleted);
            add(editor);
        }*/

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

        TaskEditor editor = new TaskEditor(new Task(serialiserFactory, timePeriod));
        editor.setOnTaskDeleted(this::onTaskDeleted);
        add(editor);
        editor.startEditing();
        editor.setOnEditCompleteHandler(this::onAddComplete);

        this.revalidate();
    }
}
