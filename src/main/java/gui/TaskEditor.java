package gui;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class TaskEditor extends JPanel {
    private final JTextField editorField;
    private final JButton labelButton;
    private final JCheckBox completeCheckbox;
    private final TaskModel taskModel;
    private Consumer<TaskEditor> editCompleteCallback;
    private Consumer<TaskEditor> onDeletedCallback;

    public TaskEditor(TaskModel taskModel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        this.taskModel = taskModel;

        editorField = new JTextField(20);
        editorField.setText(taskModel.getDescription());

        editorField.addActionListener((ActionEvent e) -> {
            stopEditing(false);
        });

        InputMap inputMap = editorField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = editorField.getActionMap();
        String cancelAction = "cancel";
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), cancelAction);
        actionMap.put(cancelAction, new CancelAction(this::stopEditing));

        editorField.addFocusListener(new FocusLossListener(this::stopEditing));

        labelButton = new JButton(taskModel.getDescription());
        labelButton.setFocusPainted(false);
        labelButton.setMargin(new Insets(0, 0, 0, 0));
        labelButton.setContentAreaFilled(false);
        labelButton.setBorderPainted(false);
        labelButton.setOpaque(false);
        labelButton.setBorder(makeBorder());

        labelButton.addActionListener((ActionEvent e) -> {
            startEditing();
        });

        completeCheckbox = new JCheckBox();
        completeCheckbox.setSelected(taskModel.getCompleted());
        completeCheckbox.addActionListener((ActionEvent e) -> {
            taskModel.setCompleted(completeCheckbox.isSelected());
        });

        add(completeCheckbox);
        add(labelButton);
    }

    public void startEditing() {
        remove(labelButton);
        add(editorField);
        editorField.selectAll();
        editorField.grabFocus();
        this.revalidate();
    }

    public void setOnEditCompleteHandler(Consumer<TaskEditor> callback) {
        editCompleteCallback = callback;
    }

    public void setOnTaskDeleted(Consumer<TaskEditor> callback) {
        onDeletedCallback = callback;
    }

    private void stopEditing(boolean cancelled) {
        if (cancelled) {
            editorField.setText(taskModel.getDescription());
        } else {
            labelButton.setText(editorField.getText());
            taskModel.setDescription(editorField.getText());
        }

        remove(editorField);
        add(labelButton);
        // need to do this for some reason to stop the border getting re-added
        if (!editorField.getText().isEmpty()) {
            labelButton.setBorder(makeBorder());
        }
        if (editorField.getText().isEmpty() && onDeletedCallback != null) {
            onDeletedCallback.accept(this);
        }
        if (editCompleteCallback != null) {
            editCompleteCallback.accept(this);
        }
        this.revalidate();
    }

    private Border makeBorder() {
        return BorderFactory.createEmptyBorder(5, 5, 5, 20);
    }

    class CancelAction extends AbstractAction {
        private final Consumer<Boolean> callback;

        public CancelAction(Consumer<Boolean> callback) {
            this.callback = callback;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            callback.accept(true);
        }
    }

    class FocusLossListener extends FocusAdapter {
        private final Consumer<Boolean> callback;

        public FocusLossListener(Consumer<Boolean> callback) {
            this.callback = callback;
        }

        @Override
        public void focusLost(FocusEvent e) {
            callback.accept(false);
        }
    }
}
