package task;

import gui.TimePeriod;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TaskDatabase {
    public final Connection connection;

    public TaskDatabase(String path) {
        String connectionString = "jdbc:sqlite:" + path;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = connection;
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS tasks (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " description TEXT, \n" +
                    " completed INTEGER NOT NULL, \n" +
                    " period_type INTEGER NOT NULL, \n" +
                    " period_date TEXT NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE INDEX IF NOT EXISTS tasks_days\n" +
                    " ON tasks(period_date, period_type)\n" +
                    " WHERE period_type = 0;";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE INDEX IF NOT EXISTS tasks_weeks\n" +
                    " ON tasks(period_date, period_type)\n" +
                    " WHERE period_type = 1;";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE INDEX IF NOT EXISTS tasks_months\n" +
                    " ON tasks(period_date, period_type)\n" +
                    " WHERE period_type = 2;";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Task> restore(Supplier<ITaskSerialiser> serialiserFactory, TimePeriod timePeriod) {
        ArrayList<Task> tasks = new ArrayList<>();

        String sql = "SELECT id FROM tasks WHERE period_type = ? AND period_date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, timePeriod.getType().ordinal());
            statement.setString(2, timePeriod.getDate().toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                long id = results.getLong(1);
                tasks.add(new Task(serialiserFactory, id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        }

        return tasks;
    }
}
