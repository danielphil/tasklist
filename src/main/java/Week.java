public class Week implements ITimePeriod {
    public final int week;
    public final int year;
    private Long id = null;

    public Week(int week, int year) {
        this.week = week;
        this.year = year;
    }

    public Week(int week, int year, long id) {
        this(week, year);
        this.id = id;
    }

    public long getId() {
        if (id == null) {
            throw new RuntimeException("ID is not set");
        }
        return id.longValue();
    }

    public boolean hasId() {
        return id != null;
    }
}
