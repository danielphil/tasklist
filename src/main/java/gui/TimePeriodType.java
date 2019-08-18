package gui;

public enum TimePeriodType {
    Day(0),
    Week(1),
    Month(2);

    private final int value;

    TimePeriodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TimePeriodType restore(int value) {
        for (TimePeriodType t : TimePeriodType.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new RuntimeException("Cannot restore value " + value);
    }
}
