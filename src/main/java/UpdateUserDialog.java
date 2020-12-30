import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class UpdateUserDialog {

    private static final int COLUMN_WIDTH = 120;
    private static final int TABLE_WIDTH = COLUMN_WIDTH * Constants.USER_COLUMNS.length;
    private static final int HEIGHT = 400;

    private Object[][] data;
    private ClientGuiController controller;

    private JFrame frame = new JFrame("Редактирование параметров пользователей");
    private JPanel pnlButton = new JPanel();
    private DefaultTableModel model = new DefaultTableModel(Constants.EMPTY_USER_TABLE, Constants.USER_COLUMNS);
    private JTable tblUsers = new JTable(model);
    private JButton btnUpdate = new JButton("Редактировать");
    private JButton btnDelete = new JButton("Удалить");
    private JButton btnCancel = new JButton("Отмена");

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UpdateUserDialog(Object[][] data, ClientGuiController controller) {
        this.data = data;
        this.controller = controller;
        init();
    }

    private void init() {

        btnUpdate.addActionListener(new UpdateUserListener());
        pnlButton.add(btnUpdate);
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tblUsers.getSelectedRowCount() > 0) {
                    String[] names = new String[tblUsers.getSelectedRowCount() + 1];
                    names[0] = "Удалить пользователя(ей) под логином(ами):";
                    int j = 1;
                    for (int i = 0; i < tblUsers.getRowCount(); i++) {
                        if (tblUsers.isRowSelected(i)) {
                            names[j] = (String) tblUsers.getValueAt(i, 0);
                            j++;
                        }
                    }
                    int result = JOptionPane.showConfirmDialog(frame, names, "Удаление пользователей",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == 0) {
                        controller.sendForDeleteUsers(names);
                        String message = controller.getModel().getNewResult().getUpdateStatus();
                        JOptionPane.showMessageDialog(frame, message, "Информация",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Не выбан ни один пользователь.",
                            "Удаление пользователей", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        pnlButton.add(btnDelete);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        pnlButton.add(btnCancel);

        int strut = TABLE_WIDTH - btnUpdate.getPreferredSize().width - btnCancel.getPreferredSize().width -
                btnDelete.getPreferredSize().width;
        pnlButton.add(Box.createHorizontalStrut(strut));

        model = new DefaultTableModel(data, Constants.USER_COLUMNS);
        tblUsers.setModel(model);

        frame.getContentPane().add(new JScrollPane(tblUsers));
        frame.getContentPane().add(pnlButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setSize(TABLE_WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void refreshTable() {
        Object[][] data = controller.sendForGetUserInfo();
        Object[] columnNames = Constants.USER_COLUMNS;
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        tblUsers.setModel(tableModel);
    }

    private class UpdateUserListener implements ActionListener {
        private JDialog dialog = new JDialog(frame, "Редактирование параметров пользователя", true);
        private DefaultTableModel tblModel;
        private JTable updTable;
        private JPanel pnlButton = new JPanel();
        private JButton btnOk = new JButton("Сохранить");
        private JButton btnNo = new JButton("Отмена");

        @Override
        public void actionPerformed(ActionEvent e) {
            Object[][] users = new Object[tblUsers.getSelectedRowCount()][Constants.USER_COLUMNS.length];
            Object[][] backUpData = new Object[tblUsers.getSelectedRowCount()][Constants.USER_COLUMNS.length];
            int k = 0;
            for (int i = 0; i < tblUsers.getRowCount(); i++) {
                if (tblUsers.isRowSelected(i)) {
                    for (int j = 0; j < users[k].length; j++) {
                        users[k][j] = tblUsers.getValueAt(i, j);
                        backUpData[k][j] = tblUsers.getValueAt(i, j);
                    }
                    k++;
                }
            }
            tblModel = new DefaultTableModel(users, Constants.USER_COLUMNS);
            updTable = new JTable(tblModel);
            for (int i = 0; i < updTable.getRowCount(); i++) {
                updTable.setEditingRow(i);
            }
            dialog.getContentPane().add(new JScrollPane(updTable));
            btnOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isEdited = false;
                    Object[][] newData = new Object[backUpData.length][Constants.USER_COLUMNS.length];
                    for (int i = 0; i < newData.length; i++) {
                        for (int j = 0; j < newData[i].length; j++) {
                            newData[i][j] = String.valueOf(updTable.getValueAt(i, j));
                            if(!newData[i][j].equals(String.valueOf(backUpData[i][j]))) {
                                isEdited = true;
                            }
                        }
                    }
                    if (isEdited) {
                        HashMap<String, User> map = new HashMap<>();
                        for (int i = 0; i < newData.length; i++) {
                            String login = (String) newData[i][0];
                            UserMode mode = UserMode.NONE;
                            switch ((String) newData[i][1]) {
                                case "user": mode = UserMode.USER;
                                    break;
                                case "admin": mode = UserMode.ADMIN;
                            }
                            String password = (String) newData[i][2];
                            map.put((String) backUpData[i][0], new User(login, password, mode));
                        }
                        controller.sendForUpdateUsers(map);
                        String message = controller.getModel().getNewResult().getUpdateStatus();
                        JOptionPane.showMessageDialog(dialog, message, "Информация",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshTable();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Не было изменено ни одного параметра пользователей",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            pnlButton.add(btnOk);
            btnNo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            pnlButton.add(btnNo);
            int strut = TABLE_WIDTH - btnOk.getPreferredSize().width - btnNo.getPreferredSize().width;
            pnlButton.add(Box.createHorizontalStrut(strut));
            dialog.getContentPane().add(pnlButton, BorderLayout.SOUTH);

            dialog.pack();
            dialog.setSize(TABLE_WIDTH, HEIGHT);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        }
    }
}
