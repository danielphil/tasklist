package task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TaskSerialiser implements ITaskSerialiser {
    private TaskDatabase database;
    private Optional<Long> id = Optional.empty();

    public TaskSerialiser(TaskDatabase database) {
        this.database = database;
    }

    public Optional<Long> getId() {
        return id;
    }

    @Override
    public void persist(Task task) {
        if (task.getDescription().isEmpty()) {
            if (id.isPresent()) {
                removeTask();
            }
        } else {
            if (id.isPresent()) {
                updateTask(task);
            } else {
                createTask(task);
            }
        }
    }

    @Override
    public void restore(long id, Task task) {
        String sql = "SELECT description, completed FROM tasks WHERE id = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new RuntimeException("Invalid ID for task");
            }
            String description = results.getString(1);
            boolean completed = results.getBoolean(2);
            this.id = Optional.of(id);
            task.setDescription(description);
            task.setCompleted(completed);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        }
    }

    private void createTask(Task task) {
        // create task
        String sql = "INSERT INTO tasks (description, completed, period_type, period_id) " +
                "VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, task.getDescription());
            statement.setBoolean(2, task.getCompleted());
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.executeUpdate();

            String query = "SELECT last_insert_rowid() AS LAST FROM tasks";
            statement = database.connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            this.id = Optional.of(result.getLong("LAST"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTask(Task task) {
        String sql = "UPDATE tasks SET description = ?, completed = ? WHERE id = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, task.getDescription());
            statement.setBoolean(2, task.getCompleted());
            statement.setLong(3, id.get());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeTask() {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setLong(1, id.get());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        id = Optional.empty();
    }
}
