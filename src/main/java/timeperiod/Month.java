package timeperiod;

import java.util.function.Supplier;

public class Month extends TimePeriod {
    private final int month;
    private final int year;

    public Month(Supplier<ITimePeriodSerialiser> serialiserFactory, int month, int year) {
        super(serialiserFactory);
        this.month = month;
        this.year = year;
        restore();
    }

    public int getMonth() {
        return month;
    }

    public int getYear() { return year; }

    @Override
    protected String getStringRepresentation() {
        return String.format("%d2-%d4", month, year);
    }

    @Override
    protected String getTableName() {
        return "months";
    }
}
