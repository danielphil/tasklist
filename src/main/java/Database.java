import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection connection;

    public Database(String path) {
        String connectionString = "jdbc:sqlite:" + path;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTables();
    }

    public void updateTask(Task task) {
        ITimePeriod period = task.getTimePeriod();

        PreparedStatement statement;

        if (task.hasId()) {
            String sql = "UPDATE tasks SET description = ?, completed = ? WHERE id = ?";
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, task.getDescription());
                statement.setBoolean(2, task.completed());
                statement.setLong(3, task.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.get    Message());
            }
        } else {
            // create task
            String sql = "INSERT INTO tasks (description, completed, period_type, period_id) " +
                    "VALUES (?, ?, ?, ?);";
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, task.getDescription());
                statement.setBoolean(2, task.completed());
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addDay(Day day) {
        // TODO: get the id of the day just added?
        try {
            // create task
            String sql = "INSERT INTO days (year, month, day) " +
                    "VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, day.day.getYear());
            statement.setInt(2, day.day.getMonthValue());
            statement.setInt(3, day.day.getDayOfMonth());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Task getTask(long id) {
        String sql = "SELECT description, completed FROM tasks WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new RuntimeException("Invalid ID for task");
            }
            return new Task(results.getString(1), results.getBoolean(2), id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        }
    }

    public ArrayList<Task> tasksForTimePeriod(ITimePeriod period) {
        return new ArrayList<>();
    }

    private void createTables() {
        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS tasks (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " description TEXT NOT NULL, \n" +
                    " completed INTEGER NOT NULL, \n" +
                    " period_type INTEGER NOT NULL, \n" +
                    " period_id INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS days (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " year INTEGER NOT NULL, \n" +
                    " month INTEGER NOT NULL, \n" +
                    " day INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS weeks (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " year INTEGER NOT NULL, \n" +
                    " week INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS months (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " year INTEGER NOT NULL, \n" +
                    " month INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
