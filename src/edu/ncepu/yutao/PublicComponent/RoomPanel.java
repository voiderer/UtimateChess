package edu.ncepu.yutao.PublicComponent;

import edu.ncepu.yutao.Network.DataStructure.RoomList;
import edu.ncepu.yutao.Network.DataStructure.RoomRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Created by AUTOY on 2016/5/1.
 */
public class RoomPanel extends JPanel {
    private JTable table;

    /**
     *
     */
    public RoomPanel() {
        super();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        String[] names = {"类型", "房间名", "服务器IP", "端口号", "人数", ""};
        Object[][] data = {};
        table = new JTable();
        table.setModel(new DefaultTableModel(data, names));
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setMinWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setMaxWidth(40);
        JScrollPane pane = new JScrollPane(table);
        pane.setOpaque(false);
        this.add(pane);
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(pane, s);
    }

    public void updateTable(RoomList roomList) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel tableModel = (DefaultTableModel) (table.getModel());
            tableModel.setRowCount(0);
            Collection<RoomRecord> collection = roomList.getCollection();
            for (RoomRecord info : collection) {
                String strings[] = new String[5];
                strings[0] = info.type;
                strings[1] = info.name;
                strings[2] = info.address.getHostAddress();
                strings[3] = "" + info.port;
                strings[4] = "" + info.playerCount;
                tableModel.addRow(strings);
            }
            table.invalidate();
        });
    }

    public String getSelected() {
        int i;
        if (-1 == (i = table.getSelectedRow())) {
            return "";
        }
        return table.getValueAt(i, 2).toString() + "|" + table.getValueAt(i, 3);
    }

}

