package timeperiod;

import task.ITaskSerialiser;
import task.Task;
import task.TaskDatabase;
import task.TaskSerialiser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class DaySerialiser implements IDaySerialiser {
    private TaskDatabase database;
    private Supplier<ITaskSerialiser> taskSerialiserFactory;
    private boolean restoring = false;

    public DaySerialiser(TaskDatabase database, Supplier<ITaskSerialiser> taskSerialiserFactory) {
        this.database = database;
        this.taskSerialiserFactory = taskSerialiserFactory;
    }

    @Override
    public void persist(Day day) {
        // Don't try to save while we're in the process of restoring a day!
        if (restoring) {
            return;
        }

        // remove all tasks for day, add new tasks
        String dayText = day.getDay().toString();

        // Remove everything for the current day
        String sql = "DELETE FROM days WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, dayText);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Add all tasks
        sql = "INSERT INTO days (date, taskId) VALUES (?, ?);";
        for (Task task : day.getTasks()) {
            try {
                PreparedStatement statement = database.connection.prepareStatement(sql);
                statement.setString(1, dayText);
                statement.setLong(2, ((TaskSerialiser)task.serialiser).getId().get());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void restore(Day day) {
        restoring = true;
        String dayText = day.getDay().toString();

        String sql = "SELECT taskId FROM days WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, dayText);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                long taskId = results.getLong(1);
                Task task = new Task(taskSerialiserFactory, taskId);
                day.addTask(task);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        } finally {
            restoring = false;
        }
    }
}
