package gui;

import task.ITaskSerialiser;
import task.TaskDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.function.Supplier;

public class MonthPanel extends JPanel {
    private final TaskDatabase db;
    private final Model<LocalDate> weekStart;
    private JPanel currentWeekGrid = null;
    private JComponent currentMonthView = null;
    private boolean isVisible = false;
    private final Supplier<ITaskSerialiser> serialiserFactory;

    public MonthPanel(TaskDatabase db, Supplier<ITaskSerialiser> serialiserFactory, Model<LocalDate> weekStart) {
        this.db = db;
        this.weekStart = weekStart;
        this.serialiserFactory = serialiserFactory;

        setDateToToday();

        setLayout(new BorderLayout());

        add(createDateSelector(), BorderLayout.NORTH);
        updatePanels();

        weekStart.observe(() -> {
            if (isVisible) {
                updatePanels();
            }
        });
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        if (isVisible) {
            updatePanels();
        }
    }

    private void updatePanels() {
        if (currentWeekGrid != null) {
            remove(currentWeekGrid);
        }
        currentWeekGrid = createDayView(serialiserFactory);
        add(currentWeekGrid, BorderLayout.CENTER);

        if (currentMonthView != null) {
            remove(currentMonthView);
        }
        currentMonthView = createMonthView(serialiserFactory);
        add(currentMonthView, BorderLayout.EAST);
    }

    private JPanel createDateSelector() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel currentWeekLabel = new JLabel();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM u");
        Runnable updateWeek = () -> currentWeekLabel.setText(weekStart.get().format(formatter));
        weekStart.observe(updateWeek);
        updateWeek.run();

        currentWeekLabel.setFont(new Font(currentWeekLabel.getFont().getName(), Font.PLAIN, 24));
        panel.add(currentWeekLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());

        JButton prevButton = new JButton("<");
        prevButton.addActionListener((ActionEvent e) -> backOneMonth());

        buttonPanel.add(prevButton);

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener((ActionEvent e) -> setDateToToday());
        buttonPanel.add(todayButton);

        JButton nextButton = new JButton(">");
        nextButton.addActionListener((ActionEvent e) -> forwardOneMonth());
        buttonPanel.add(nextButton);

        buttonPanel.add(Box.createHorizontalGlue());

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDayView(Supplier<ITaskSerialiser> serialiserFactory) {
        LocalDate date = weekStart.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E d MMM");

        JPanel weekGrid = new JPanel(new GridLayout(5, 0));
        for (int i = 0; i < 5; i++) {
            JPanel dayGrid = new JPanel(new GridLayout(0, 2));

            JScrollPane scrollPane = new JScrollPane(new TaskList(db, serialiserFactory, new TimePeriod(TimePeriodType.Day, date)));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            dayGrid.add(new JLabel(date.format(formatter)));
            dayGrid.add(scrollPane);

            JPanel dayAndBorder = new JPanel();
            dayAndBorder.setLayout(new BoxLayout(dayAndBorder, BoxLayout.PAGE_AXIS));
            dayAndBorder.add(dayGrid);
            dayAndBorder.add(new JSeparator(SwingConstants.HORIZONTAL));

            weekGrid.add(dayAndBorder);

            date = date.plusDays(1);
        }
        return weekGrid;
    }

    private JComponent createMonthView(Supplier<ITaskSerialiser> serialiserFactory) {
        LocalDate firstDayOfMonth = weekStart.get().withDayOfMonth(1);
        JScrollPane scrollPane = new JScrollPane(new TaskList(db, serialiserFactory, new TimePeriod(TimePeriodType.Month, firstDayOfMonth)));
        scrollPane.setPreferredSize(new Dimension(250, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void setDateToToday() {
        LocalDate now = LocalDate.now();
        TemporalField field = WeekFields.of(Locale.UK).dayOfWeek();
        weekStart.set(now.with(field, 1));
    }

    private void backOneMonth() {
        LocalDate newDate = weekStart.get().minusMonths(1);
        newDate = newDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        weekStart.set(newDate);

    }

    private void forwardOneMonth() {
        LocalDate newDate = weekStart.get().plusMonths(1);
        newDate = newDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        weekStart.set(newDate);
    }
}
