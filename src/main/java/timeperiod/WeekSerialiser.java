package timeperiod;

import task.ITaskSerialiser;
import task.Task;
import task.TaskDatabase;
import task.TaskSerialiser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class WeekSerialiser implements IWeekSerialiser {
    private TaskDatabase database;
    private Supplier<ITaskSerialiser> taskSerialiserFactory;
    private boolean restoring = false;

    public WeekSerialiser(TaskDatabase database, Supplier<ITaskSerialiser> taskSerialiserFactory) {
        this.database = database;
        this.taskSerialiserFactory = taskSerialiserFactory;
    }

    @Override
    public void persist(Week week) {
        // Don't try to save while we're in the process of restoring a day!
        if (restoring) {
            return;
        }

        // remove all tasks for day, add new tasks
        String weekText = getWeekString(week);

        // Remove everything for the current day
        String sql = "DELETE FROM weeks WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, weekText);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Add all tasks
        sql = "INSERT INTO days (date, taskId) VALUES (?, ?);";
        for (Task task : week.getTasks()) {
            try {
                PreparedStatement statement = database.connection.prepareStatement(sql);
                statement.setString(1, weekText);
                statement.setLong(2, ((TaskSerialiser)task.serialiser).getId().get());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void restore(Week week) {
        restoring = true;
        String weekText = getWeekString(week);

        String sql = "SELECT taskId FROM week WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, weekText);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                long taskId = results.getLong(1);
                Task task = new Task(taskSerialiserFactory, taskId);
                week.addTask(task);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        } finally {
            restoring = false;
        }
    }

    private static String getWeekString(Week week) {
        return String.format("%d2-%d4", week.getWeekNumber(), week.getYear());
    }
}
