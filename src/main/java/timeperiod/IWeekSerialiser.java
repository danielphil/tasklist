package timeperiod;

public interface IWeekSerialiser {
    void persist(Week week);
    void restore(Week week);
}
