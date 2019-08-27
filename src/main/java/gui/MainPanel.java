package gui;

import task.TaskDatabase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.time.LocalDate;
import java.util.function.Supplier;

public class MainPanel extends JPanel {
    public MainPanel(TaskDatabase db) {
        setLayout(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        Model<LocalDate> weekStart = new Model<>();
        WeekPanel weekPanel = new WeekPanel(db, weekStart);
        MonthPanel monthPanel = new MonthPanel(db, weekStart);

        tabbedPane.addChangeListener((ChangeEvent e) -> {
            int index = tabbedPane.getSelectedIndex();
            weekPanel.setIsVisible(index == 0);
            monthPanel.setIsVisible(index == 1);
        });

        tabbedPane.addTab("Week", weekPanel);
        tabbedPane.addTab("Month", monthPanel);
        add(tabbedPane);
    }
}
