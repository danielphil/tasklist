public class Month implements ITimePeriod {
    public final int month;
    public final int year;
    private Long id = null;

    public Month(int month, int year) {
        this.year = year;
        this.month = month;
    }

    public Month(int month, int year, long id) {
        this(month, year);
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
