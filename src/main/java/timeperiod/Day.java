package timeperiod;

import task.Task;

import java.time.LocalDate;
import java.util.function.Supplier;

public class Day extends TimePeriod {
    private final LocalDate day;

    public Day(Supplier<ITimePeriodSerialiser> serialiserFactory, LocalDate day) {
        super(serialiserFactory);
        this.day = day;
        restore();
    }

    public LocalDate getDay() {
        return day;
    }

    @Override
    protected String getStringRepresentation() {
        return day.toString();
    }

    @Override
    protected String getTableName() {
        return "days";
    }
}
