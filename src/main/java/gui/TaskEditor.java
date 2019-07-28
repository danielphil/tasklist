package gui;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TaskEditor extends JPanel {
    private final JTextField editorField;
    private final JButton labelButton;
    private final JCheckBox completeCheckbox;
    private final TaskModel taskModel;

    public TaskEditor(TaskModel taskModel) {
        super(new FlowLayout(FlowLayout.LEFT));

        this.taskModel = taskModel;

        editorField = new JTextField(20);
        editorField.setText(taskModel.getDescription());

        editorField.addActionListener((ActionEvent e) -> {
            stopEditing();
        });

        InputMap inputMap = editorField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = editorField.getActionMap();
        String cancelAction = "cancel";
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), cancelAction);
        actionMap.put(cancelAction, new CancelAction(() -> stopEditing()));

        editorField.addFocusListener(new FocusLossListener(() -> stopEditing()));

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

    private void startEditing() {
        remove(labelButton);
        add(editorField);
        editorField.selectAll();
        editorField.grabFocus();
        this.revalidate();
    }

    private void stopEditing() {
        labelButton.setText(editorField.getText());
        taskModel.setDescription(editorField.getText());

        remove(editorField);
        add(labelButton);
        // need to do this for some reason to stop the border getting re-added
        if (!editorField.getText().isEmpty()) {
            labelButton.setBorder(makeBorder());
        }
        this.revalidate();
        this.getRootPane().repaint();
    }

    private Border makeBorder() {
        return BorderFactory.createEmptyBorder(5, 5, 5, 20);
    }

    class CancelAction extends AbstractAction {
        private final Runnable callback;

        public CancelAction(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            callback.run();
        }
    }

    class FocusLossListener extends FocusAdapter {
        private final Runnable callback;

        public FocusLossListener(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void focusLost(FocusEvent e) {
            callback.run();
        }
    }
}
