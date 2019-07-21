package task;

import java.sql.*;

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
                    " period_id INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS days (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " date TEXT NOT NULL, \n" +
                    " taskId INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS weeks (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " date INTEGER NOT NULL, \n" +
                    " taskId INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            String task_create = "CREATE TABLE IF NOT EXISTS months (\n" +
                    " id INTEGER PRIMARY KEY, \n" +
                    " date INTEGER NOT NULL, \n" +
                    " taskId INTEGER NOT NULL);";
            statement.execute(task_create);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
