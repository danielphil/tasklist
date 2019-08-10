package gui;

import task.ITaskSerialiser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.function.Supplier;

public class WeekPanel extends JPanel {
    private Model<LocalDate> weekStart = new Model<>();

    public WeekPanel(Supplier<ITaskSerialiser> serialiserFactory) {
        setDateToToday();

        setLayout(new BorderLayout());

        add(createDateSelector(), BorderLayout.NORTH);
        add(createDayView(serialiserFactory), BorderLayout.CENTER);
        add(createWeekView(serialiserFactory), BorderLayout.EAST);
    }

    private JPanel createDateSelector() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel currentWeekLabel = new JLabel();
        Runnable updateWeek = () -> currentWeekLabel.setText(weekStart.get().toString());
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

    private JPanel createDayView(Supplier<ITaskSerialiser> serialiserFactory) {
        JPanel weekGrid = new JPanel(new GridLayout(5, 0));
        for (int i = 0; i < 5; i++) {
            JPanel dayGrid = new JPanel(new GridLayout(0, 2));

            JScrollPane scrollPane = new JScrollPane(new TaskList(serialiserFactory));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            dayGrid.add(new JLabel("Monday 22 July 2019"));
            dayGrid.add(scrollPane);

            JPanel dayAndBorder = new JPanel();
            dayAndBorder.setLayout(new BoxLayout(dayAndBorder, BoxLayout.PAGE_AXIS));
            dayAndBorder.add(dayGrid);
            dayAndBorder.add(new JSeparator(SwingConstants.HORIZONTAL));

            weekGrid.add(dayAndBorder);
        }
        return weekGrid;
    }

    private JComponent createWeekView(Supplier<ITaskSerialiser> serialiserFactory) {
        JScrollPane scrollPane = new JScrollPane(new TaskList(serialiserFactory));
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

    private void updateViews() {

    }
}
