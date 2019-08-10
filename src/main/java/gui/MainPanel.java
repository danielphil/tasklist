package gui;

import task.ITaskSerialiser;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class MainPanel extends JPanel {
    public MainPanel(Supplier<ITaskSerialiser> serialiserFactory) {
        setLayout(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Week", new WeekPanel(serialiserFactory));
        tabbedPane.addTab("Month", new JPanel());
        add(tabbedPane);
    }
}
