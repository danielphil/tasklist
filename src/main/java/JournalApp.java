import gui.MainPanel;
import task.Task;

import javax.swing.*;
import java.util.function.Supplier;

public class JournalApp {
    public static void main(String[] args) {
        task.TaskDatabase db = new task.TaskDatabase("testtask.db");
        javax.swing.SwingUtilities.invokeLater(() -> createGui(db));
    }

    public static void createGui(task.TaskDatabase db) {
        JFrame frame = new JFrame("HelloWorldSwing!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new MainPanel(db));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
