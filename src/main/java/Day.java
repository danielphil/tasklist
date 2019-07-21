import java.time.LocalDate;

public class Day implements ITimePeriod {
    public final LocalDate day;
    private Long id = null;

    public Day(LocalDate day) {
        this.day = day;
    }

    public Day(LocalDate day, long id) {
        this(day);
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
