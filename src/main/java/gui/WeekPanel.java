package gui;

import task.TaskDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.function.Supplier;

public class WeekPanel extends JPanel {
    private final TaskDatabase db;
    private final Model<LocalDate> weekStart;
    private JPanel currentWeekGrid = null;
    private JComponent currentWeekView = null;
    private boolean isVisible = false;

    public WeekPanel(TaskDatabase db, Model<LocalDate> weekStart) {
        this.db = db;
        this.weekStart = weekStart;

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
        currentWeekGrid = createDayView();
        add(currentWeekGrid, BorderLayout.CENTER);

        if (currentWeekView != null) {
            remove(currentWeekView);
        }
        currentWeekView = createWeekView();
        add(currentWeekView, BorderLayout.EAST);
    }

    private JPanel createDateSelector() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel currentWeekLabel = new JLabel();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("w u");
        Runnable updateWeek = () -> currentWeekLabel.setText("Week " + weekStart.get().format(formatter));
        weekStart.observe(updateWeek);
        updateWeek.run();

        currentWeekLabel.setFont(new Font(currentWeekLabel.getFont().getName(), Font.PLAIN, 24));
        panel.add(currentWeekLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());

        JButton prevButton = new JButton("<");
        prevButton.addActionListener((ActionEvent e) -> backOneWeek());

        buttonPanel.add(prevButton);

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener((ActionEvent e) -> setDateToToday());
        buttonPanel.add(todayButton);

        JButton nextButton = new JButton(">");
        nextButton.addActionListener((ActionEvent e) -> forwardOneWeek());
        buttonPanel.add(nextButton);

        buttonPanel.add(Box.createHorizontalGlue());

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDayView() {
        LocalDate date = weekStart.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E d MMM");

        JPanel weekGrid = new JPanel(new GridLayout(5, 0));
        for (int i = 0; i < 5; i++) {
            JPanel dayGrid = new JPanel(new GridLayout(0, 2));

            JScrollPane scrollPane = new JScrollPane(new TaskList(db, new TimePeriod(TimePeriodType.Day, date)));
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

    private JComponent createWeekView() {
        // TODO: Need to fix the local date to the start of the week below
        JScrollPane scrollPane = new JScrollPane(new TaskList(db, new TimePeriod(TimePeriodType.Week, weekStart.get())));
        scrollPane.setPreferredSize(new Dimension(250, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void setDateToToday() {
        LocalDate now = LocalDate.now();
        TemporalField field = WeekFields.of(Locale.UK).dayOfWeek();
        weekStart.set(now.with(field, 1));
    }

    private void backOneWeek() {
        weekStart.set(weekStart.get().minusWeeks(1));
    }

    private void forwardOneWeek() {
        weekStart.set(weekStart.get().plusWeeks(1));
    }
}
