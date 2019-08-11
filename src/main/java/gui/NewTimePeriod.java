package gui;

import java.time.LocalDate;

public class NewTimePeriod {
    private TimePeriodType timePeriodType;
    private LocalDate date;

    public NewTimePeriod(TimePeriodType type, LocalDate date) {
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
