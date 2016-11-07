package edu.ncepu.yutao.MainClass;

import edu.ncepu.yutao.Chess.ImageManager;
import edu.ncepu.yutao.Persistence.PersistenceManager;
import org.jdesktop.swingx.JXComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;


class LoginWindow extends JDialog {
    private JXComboBox comboBox;
    private boolean bOk;

    LoginWindow() {
        LinkedList<String> names = PersistenceManager.getUserList().getList();
        JPanel contentPane = new JPanel();
        this.setLocationRelativeTo(null);
        this.setTitle("登录");
        JButton buttonOK = new JButton("确定");
        JButton buttonCancel = new JButton("取消");
        comboBox = new JXComboBox(names.toArray());
        comboBox.setEditable(true);
        JTextField field = (JTextField) comboBox.getEditor().getEditorComponent();
        field.setColumns(13);
        contentPane.add(comboBox, BorderLayout.CENTER);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(buttonOK);
        panel.add(buttonCancel);
        contentPane.add(panel, BorderLayout.SOUTH);
        setContentPane(contentPane);
        setModal(true);
        setSize(230, 110);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(ImageManager.icon.getImage());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onOK() {
        String string = comboBox.getEditor().getItem().toString();
        if (string.isEmpty()) {
            this.setTitle("名称不可为空");
            return;
        }
        PersistenceManager.readUserData(string);
        PersistenceManager.saveChessData();
        PersistenceManager.saveUserList();
        bOk = true;
        setVisible(false);
    }

    private void onCancel() {
        PersistenceManager.saveUserList();
        bOk = false;
        setVisible(false);
    }

    boolean isbOk() {
        return bOk;
    }
}

