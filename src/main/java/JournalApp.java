import gui.MainPanel;
import task.ITaskSerialiser;

import javax.swing.*;
import java.util.function.Supplier;

public class JournalApp {
    public static void main(String[] args) {
        task.TaskDatabase db = new task.TaskDatabase("testtask.db");
        Supplier<ITaskSerialiser> createSerialiser = () -> new task.TaskSerialiser(db);
        javax.swing.SwingUtilities.invokeLater(() -> createGui(db, createSerialiser));
    }

    public static void createGui(task.TaskDatabase db, Supplier<ITaskSerialiser> serialiserFactory) {
        JFrame frame = new JFrame("HelloWorldSwing!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new MainPanel(db, serialiserFactory));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
