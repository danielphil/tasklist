package timeperiod;

public interface ITimePeriodSerialiser {
    void persist(TimePeriod timePeriod);
    void restore(TimePeriod timePeriod);
}
