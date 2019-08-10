package gui;

import task.ITaskSerialiser;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class WeekPanel extends JPanel {
    public WeekPanel(Supplier<ITaskSerialiser> serialiserFactory) {
        setLayout(new BorderLayout());

        add(createDateSelector(), BorderLayout.NORTH);
        add(createDayView(serialiserFactory), BorderLayout.CENTER);
        add(createWeekView(serialiserFactory), BorderLayout.EAST);
        /*
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.2;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Something goes here!"), c);

        JPanel weekView = createWeekView(serialiserFactory);
        c.gridy = 1;
        c.weighty = 0.8;
        c.anchor = GridBagConstraints.PAGE_END;
        c.fill = GridBagConstraints.BOTH;
        add(weekView, c);

         */
    }

    private JPanel createDateSelector() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel currentWeekLabel = new JLabel("Week 23 2019");
        currentWeekLabel.setFont(new Font(currentWeekLabel.getFont().getName(), Font.PLAIN, 24));
        panel.add(currentWeekLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JButton("<"));
        buttonPanel.add(new JButton("Today"));
        buttonPanel.add(new JButton(">"));
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
}
