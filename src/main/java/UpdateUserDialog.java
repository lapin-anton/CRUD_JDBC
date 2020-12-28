import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UpdateUserDialog {

    private static final int COLUMN_WIDTH = 120;
    private static final int TABLE_WIDTH = COLUMN_WIDTH * Constants.USER_COLUMNS.length;
    private static final int HEIGHT = 400;

    private Object[][] data;

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

    public UpdateUserDialog(Object[][] data) {
        this.data = data;
        init();
    }

    private void init() {

        pnlButton.add(btnUpdate);
        pnlButton.add(btnDelete);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
