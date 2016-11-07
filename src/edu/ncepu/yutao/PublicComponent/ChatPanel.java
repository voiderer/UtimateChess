package edu.ncepu.yutao.PublicComponent;

import edu.ncepu.yutao.PublicComponent.Event.ChatPanelListener;
import org.pushingpixels.substance.internal.ui.SubstanceScrollPaneUI;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by AUTOY on 2016/4/29
 */
public class ChatPanel extends JPanel {
    private JTextField textField;
    private JTextPane textPane;
    private JButton buttonSend;

    public ChatPanel() {
        super();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        textField = new JTextField();
        textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setEditable(false);
        this.setOpaque(false);
        JScrollPane scrollPane1 = new JScrollPane(textPane);
        scrollPane1.setOpaque(false);
        scrollPane1.setUI(new SubstanceScrollPaneUI());
        buttonSend = new JButton("发送");
        buttonSend.addActionListener(e -> onSend());
        this.add(scrollPane1);
        this.add(textField);
        this.add(buttonSend);
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(scrollPane1, s);
        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(textField, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(buttonSend, s);
        this.setPreferredSize(new Dimension(300, 100));
        this.registerKeyboardAction(e -> onSend(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    public void appendLn(String string, Color color) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(attributeSet, 14);
        StyleConstants.setForeground(attributeSet, color);
        Document docs = textPane.getDocument();
        try {
            docs.insertString(docs.getLength(), string + "\n", attributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        textPane.setCaretPosition(docs.getLength());
    }

    private void onSend() {
        String context = textField.getText();
        if (context.equals("")) {
            return;
        }
        fireMessageSent(context);
        textField.setText("");
    }

    public void addChatPanelListener(ChatPanelListener l) {
        listenerList.add(ChatPanelListener.class, l);
    }

    public void removeChatPanelListener(ChatPanelListener l) {
        listenerList.remove(ChatPanelListener.class, l);
    }

    protected void fireMessageSent(String message) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChatPanelListener.class) {
                ((ChatPanelListener) listeners[i + 1]).messageSent(message);
            }
        }
    }

}
