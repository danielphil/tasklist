package timeperiod;

import task.ITaskSerialiser;
import task.Task;
import task.TaskDatabase;
import task.TaskSerialiser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class TimePeriodSerialiser implements ITimePeriodSerialiser {
    private TaskDatabase database;
    private Supplier<ITaskSerialiser> taskSerialiserFactory;
    private boolean restoring = false;

    public TimePeriodSerialiser(TaskDatabase database, Supplier<ITaskSerialiser> taskSerialiserFactory) {
        this.database = database;
        this.taskSerialiserFactory = taskSerialiserFactory;
    }

    @Override
    public void persist(TimePeriod timePeriod) {
        // Don't try to save while we're in the process of restoring a day!
        if (restoring) {
            return;
        }

        // remove all tasks for day, add new tasks
        String dateText = timePeriod.getStringRepresentation();

        // Remove everything for the current day
        String sql = "DELETE FROM " + timePeriod.getTableName() + " WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, dateText);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Add all tasks
        sql = "INSERT INTO " + timePeriod.getTableName() + " (date, taskId) VALUES (?, ?);";
        for (Task task : timePeriod.getTasks()) {
            try {
                PreparedStatement statement = database.connection.prepareStatement(sql);
                statement.setString(1, dateText);
                statement.setLong(2, ((TaskSerialiser)task.serialiser).getId().get());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void restore(TimePeriod timePeriod) {
        restoring = true;
        String dayText = timePeriod.getStringRepresentation();

        String sql = "SELECT taskId FROM " + timePeriod.getTableName() + " WHERE date = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, dayText);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                long taskId = results.getLong(1);
                Task task = new Task(taskSerialiserFactory, taskId);
                timePeriod.addTask(task);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        } finally {
            restoring = false;
        }
    }
}
