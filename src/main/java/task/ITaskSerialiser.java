package task;

public interface ITaskSerialiser {
    void persist(Task task);
    void restore(long id, Task task);
}
