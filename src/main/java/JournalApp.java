import gui.MainPanel;
import task.Task;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class JournalApp {
    public static void main(String[] args) {
        task.TaskDatabase db = new task.TaskDatabase("testtask.db");
        javax.swing.SwingUtilities.invokeLater(() -> createGui(db));
    }

    public static void createGui(task.TaskDatabase db) {
        JFrame frame = new JFrame("HelloWorldSwing!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //frame.getContentPane().add(new MainPanel(db));

        var model = new MyTableModel();
        final JTable table = new JTable(model);
        table.setTableHeader(null);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.getColumnModel().getColumn(0).setMaxWidth(27);

        /*
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.GRAY);
        */

        table.setFillsViewportHeight(true);

        InputMap im = table.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap am = table.getActionMap();

        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeRows(table.getSelectedRows());
            }
        };

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Delete");
        am.put("Delete", deleteAction);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        frame.getContentPane().add(scrollPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"Done", "Description" };

        private ArrayList<Object[]> data = new ArrayList<>(Arrays.asList(
            new Object[]{false, "Eat toast"},
            new Object[]{true, "buy bananas"},
            new Object[]{false, "go to sleep"}
        ));

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size() + 1;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (row == getRowCount() - 1 && col == 0) {
                return false;
            } else if (row == getRowCount() - 1 && col == 1) {
                return "";
            } else {
                return data.get(row)[col];
            }
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (row == getRowCount() - 1 && col == 0) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            // don't let the user change the checkbox value of the empty row

            if (row != getRowCount() - 1 && col == 1 && ((String) value).isEmpty()) {
                removeRows(new int[]{row});
            } else if (row == getRowCount() - 1 && col == 1) {
                var str = (String) value;
                if (!str.isEmpty()) {
                    data.add(new Object[]{false, value});
                    fireTableDataChanged();
                } else {
                    // If we don't do this, the cell is highlighted red and need
                    // to press escape to get out of the editor.
                    fireTableCellUpdated(row, col);
                }
            } else {
                data.get(row)[col] = value;
                fireTableCellUpdated(row, col);
            }
        }

        public void removeRows(int[] rows) {
            Arrays.sort(rows);

            int offset = 0;
            for (int element : rows) {
                data.remove(element + offset);
                --offset;
            }

            fireTableDataChanged();
        }
    }
}
