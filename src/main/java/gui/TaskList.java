package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskList extends JPanel {
    private final TaskListModel model;
    private final JButton addNewButton;

    public TaskList(TaskListModel model) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.model = model;

        for (TaskModel task : model.tasks()) {
            TaskEditor editor = new TaskEditor(task);
            editor.setOnTaskDeleted(this::onTaskDeleted);
            add(editor);
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

        TaskEditor editor = new TaskEditor(new TaskModel());
        editor.setOnTaskDeleted(this::onTaskDeleted);
        add(editor);
        editor.startEditing();
        editor.setOnEditCompleteHandler(this::onAddComplete);

        this.revalidate();
    }
}
