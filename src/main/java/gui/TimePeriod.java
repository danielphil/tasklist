package gui;

import java.time.LocalDate;

public class TimePeriod {
    private TimePeriodType timePeriodType;
    private LocalDate date;

    public TimePeriod(TimePeriodType type, LocalDate date) {
        timePeriodType = type;
        this.date = date;
    }

    public TimePeriodType getType() {
        return timePeriodType;
    }

    public LocalDate getDate() {
        return date;
    }
}
