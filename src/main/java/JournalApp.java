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
        task.Task t = new task.Task(createSerialiser);
        t.setDescription("Do something");
        t.setCompleted(true);
        long id = ((TaskSerialiser)t.serialiser).getId().get();

        Supplier<ITimePeriodSerialiser> daySerializer = () -> new TimePeriodSerialiser(db, createSerialiser);
        timeperiod.Day day = new Day(daySerializer, LocalDate.now());
        day.addTask(t);

        day = new Day(daySerializer, LocalDate.now());
        System.out.println(day.getDay().toString());
        for (task.Task task : day.getTasks()) {
            System.out.println(task.getDescription());
            System.out.println(task.getCompleted());
            task.setCompleted(false);
        }

        javax.swing.SwingUtilities.invokeLater(() -> createGui(createSerialiser));
    }

    public static void createGui(Supplier<ITaskSerialiser> serialiserFactory) {
        JFrame frame = new JFrame("HelloWorldSwing!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new MainPanel(serialiserFactory));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
