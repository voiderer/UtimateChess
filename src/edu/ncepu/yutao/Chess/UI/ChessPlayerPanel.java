package edu.ncepu.yutao.Chess.UI;

import edu.ncepu.yutao.Chess.DataStructure.ChessClientList;
import edu.ncepu.yutao.Chess.DataStructure.ChessClientRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Created by AUTOY on 2016/6/10.
 */
public class ChessPlayerPanel extends JPanel {
    private JTable table;

    public ChessPlayerPanel() {
        super();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        String[] names = {"用户名", "IP地址", "端口", "身份"};
        String[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, names);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(20);
        JScrollPane pane = new JScrollPane(table);
        pane.setOpaque(false);
        this.add(pane);
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(pane, s);
        this.setPreferredSize(new Dimension(300, 200));
    }

    public void updateTable(ChessClientList clientList) {
        DefaultTableModel tableModel = (DefaultTableModel) (table.getModel());
        tableModel.setRowCount(0);
        Collection<ChessClientRecord> collection = clientList.getCollection();
        for (ChessClientRecord info : collection) {
            String strings[] = new String[4];
            strings[0] = info.name;
            strings[1] = info.address.getHostAddress();
            strings[2] = "" + info.port;
            strings[3] = info.type.toString();
            tableModel.addRow(strings);
        }
        table.invalidate();

    }
}
