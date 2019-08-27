package task;

import gui.TimePeriod;
import gui.TimePeriodType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class SqlLiteTask extends Task {
    private final TaskDatabase database;
    private Optional<Long> id = Optional.empty();

    public SqlLiteTask(TaskDatabase database, TimePeriod timePeriod) {
        super(timePeriod);
        this.database = database;
        persist();
    }

    public SqlLiteTask(TaskDatabase database, long id) {
        this.database = database;
        this.id = Optional.of(id);
        restore();
    }

    public Optional<Long> getId() {
        return id;
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        persist();
    }

    @Override
    public void setCompleted(boolean completed) {
        super.setCompleted(completed);
        persist();
    }

    @Override
    public void setTimePeriod(TimePeriod timePeriod) {
        super.setTimePeriod(timePeriod);
        persist();
    }

    private void persist() {
        if (getDescription().isEmpty()) {
            if (id.isPresent()) {
                removeTask();
            }
        } else {
            if (id.isPresent()) {
                updateTask();
            } else {
                createTask();
            }
        }
    }

    private void restore() {
        String sql = "SELECT description, completed, period_type, period_date FROM tasks WHERE id = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setLong(1, id.get());
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new RuntimeException("Invalid ID for task");
            }
            String description = results.getString(1);
            boolean completed = results.getBoolean(2);
            TimePeriodType type = TimePeriodType.restore(results.getInt(3));
            String periodDate = results.getString(4);

            // Call the base class here to avoid attempting to serialise new values
            super.setDescription(description);
            super.setCompleted(completed);
            super.setTimePeriod(new TimePeriod(type, LocalDate.parse(periodDate)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get task");
        }
    }

    private void createTask() {
        // create task
        String sql = "INSERT INTO tasks (description, completed, period_type, period_date) " +
                "VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, getDescription());
            statement.setBoolean(2, getCompleted());
            statement.setInt(3, getTimePeriod().getType().getValue());
            statement.setString(4, getTimePeriod().getDate().toString());
            statement.executeUpdate();

            String query = "SELECT last_insert_rowid() AS LAST FROM tasks";
            statement = database.connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            this.id = Optional.of(result.getLong("LAST"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTask() {
        String sql = "UPDATE tasks SET description = ?, completed = ?, period_type = ?, period_date = ? WHERE id = ?";
        try {
            PreparedStatement statement = database.connection.prepareStatement(sql);
            statement.setString(1, getDescription());
            statement.setBoolean(2, getCompleted());
            statement.setInt(3, getTimePeriod().getType().getValue());
            statement.setString(4, getTimePeriod().getDate().toString());
            statement.setLong(5, id.get());
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
