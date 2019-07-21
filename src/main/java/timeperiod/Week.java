package timeperiod;

import task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Week extends TimePeriod {
    private final int weekNumber;
    private final int year;

    public Week(Supplier<ITimePeriodSerialiser> serialiserFactory, int weekNumber, int year) {
        super(serialiserFactory);
        this.weekNumber = weekNumber;
        this.year = year;
        restore();
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getYear() { return year; }

    @Override
    protected String getStringRepresentation() {
        return String.format("%d2-%d4", weekNumber, year);
    }

    @Override
    protected String getTableName() {
        return "weeks";
    }
}
