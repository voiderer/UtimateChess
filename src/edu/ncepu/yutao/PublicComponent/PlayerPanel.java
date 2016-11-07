package edu.ncepu.yutao.PublicComponent;


import edu.ncepu.yutao.Network.DataStructure.PeerList;
import edu.ncepu.yutao.Network.DataStructure.PeerRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Created by AUTOY on 2016/4/30.
 */
public class PlayerPanel extends JPanel {
    private JTable table;

    public PlayerPanel() {
        super();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        String[] names = {"用户名", "IP地址", "端口"};
        String[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, names);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        JScrollPane pane = new JScrollPane(table);
        pane.setOpaque(false);
        this.add(pane);
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(pane, s);
        this.setPreferredSize(new Dimension(220, 200));
    }

    public void updateTable(PeerList peerList) {
        DefaultTableModel tableModel = (DefaultTableModel) (table.getModel());
        tableModel.setRowCount(0);
        Collection<PeerRecord> collection = peerList.getCollection();
        for (PeerRecord info : collection) {
            String strings[] = new String[4];
            strings[0] = info.name;
            strings[1] = info.address.getHostAddress();
            strings[2] = "" + info.port;
            tableModel.addRow(strings);
        }
        table.invalidate();

    }
}
