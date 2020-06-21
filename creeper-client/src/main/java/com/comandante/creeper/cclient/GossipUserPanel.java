package com.comandante.creeper.cclient;

import com.comandante.creeper.chat.Gossip;
import com.google.common.eventbus.Subscribe;
import com.terminal.ui.ColorPane;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

public class GossipUserPanel extends JPanel {

    private final JList<UserListItem> userListItems;
    private final DefaultListModel<UserListItem> defaultListModel;
    private final TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green), "Users");

    public GossipUserPanel() {
        defaultListModel = new DefaultListModel<>();
        userListItems = new JList<>(defaultListModel);
        userListItems.setBackground(Color.BLACK);
        userListItems.setCellRenderer(new UserPaneCellRenderer());

        JScrollPane pane = new JScrollPane(userListItems);
        pane.setBorder(BorderFactory.createEmptyBorder());

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(border);

        add(pane);
        setFocusable(false);
        setVisible(true);

    }

    public static class UserPaneCellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            UserListItem userListItem = (UserListItem) value;
            ColorPane colorPane = new ColorPane();
            colorPane.setOpaque(true);
            colorPane.appendANSI(userListItem.getName());
            if (isSelected) {
                colorPane.setBackground(Color.darkGray);
            } else {
                colorPane.setBackground(Color.BLACK);
            }
            return colorPane;
        }
    }

    public static class UserListItem {
        private final String name;

        public UserListItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Subscribe
    public void gossipEvent(Gossip gossip) throws IOException {
        defaultListModel.removeAllElements();
        gossip.getUserMap().values().stream().sorted().forEach(s -> defaultListModel.addElement(new UserListItem(s)));
        userListItems.revalidate();
        userListItems.repaint();
    }
}
