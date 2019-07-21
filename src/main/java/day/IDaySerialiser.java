package day;

import task.ITaskSerialiser;

import java.util.function.Supplier;

public interface IDaySerialiser {
    void persist(Day day);
    void restore(Day day);
}
