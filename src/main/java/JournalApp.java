import day.Day;
import day.IDaySerialiser;
import task.ITaskSerialiser;
import task.TaskSerialiser;

import java.time.LocalDate;
import java.util.function.Supplier;

public class JournalApp {
    public static void main(String[] args) {
        /*
        Task t = new Task("", false, 2);
        t.setCompleted(true);
        t.setDescription("Here's an updated task");
        System.out.println(t);
        System.out.println("hello!");
        Database db = new Database("testfile.db");
        db.updateTask(t);
        Task restored = db.getTask(3);
        System.out.println(restored);
        */

        task.TaskDatabase db = new task.TaskDatabase("testtask.db");
        Supplier<ITaskSerialiser> createSerialiser = () -> new task.TaskSerialiser(db);
        task.Task t = new task.Task(createSerialiser);
        t.setDescription("Do something");
        t.setCompleted(true);
        long id = ((TaskSerialiser)t.serialiser).getId().get();

        Supplier<IDaySerialiser> daySerializer = () -> new day.DaySerialiser(db, createSerialiser);
        day.Day day = new Day(daySerializer, LocalDate.now());
        day.addTask(t);

        day = new Day(daySerializer, LocalDate.now());
        System.out.println(day.getDay().toString());
        for (task.Task task : day.getTasks()) {
            System.out.println(task.getDescription());
            System.out.println(task.getCompleted());
            task.setCompleted(false);
        }
    }
}
