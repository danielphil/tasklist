public class JournalApp {
    public static void main(String[] args) {
        Task t = new Task("", false, 2);
        t.setCompleted(true);
        t.setDescription("Here's an updated task");
        System.out.println(t);
        System.out.println("hello!");
        Database db = new Database("testfile.db");
        db.updateTask(t);
        Task restored = db.getTask(3);
        System.out.println(restored);
    }
}
