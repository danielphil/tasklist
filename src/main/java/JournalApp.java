import gui.MainPanel;
import gui.TaskList;
import gui.TaskListModel;
import gui.TaskModel;
import timeperiod.Day;
import task.ITaskSerialiser;
import task.TaskSerialiser;
import timeperiod.ITimePeriodSerialiser;
import timeperiod.TimePeriodSerialiser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
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
