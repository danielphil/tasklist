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

        javax.swing.SwingUtilities.invokeLater(() -> createGui());
    }

    public static void createGui() {
        JFrame frame = new JFrame("HelloWorldSwing!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel weekGrid = new JPanel(new GridLayout(5, 0));
        for (int i = 0; i < 5; i++) {
            JPanel dayGrid = new JPanel(new GridLayout(0, 2));

            JPanel taskPanel = new gui.TaskEditor(new TaskModel("empty task", true));

            dayGrid.add(new JLabel("Monday 22 July 2019"));
            dayGrid.add(taskPanel);

            JPanel dayAndBorder = new JPanel();
            dayAndBorder.setLayout(new BoxLayout(dayAndBorder, BoxLayout.PAGE_AXIS));
            dayAndBorder.add(dayGrid);
            dayAndBorder.add(new JSeparator(SwingConstants.HORIZONTAL));

            weekGrid.add(dayAndBorder);
        }

        frame.getContentPane().add(weekGrid);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
