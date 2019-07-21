package timeperiod;

public interface IDaySerialiser {
    void persist(Day day);
    void restore(Day day);
}
