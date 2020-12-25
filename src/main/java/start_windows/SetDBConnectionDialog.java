package start_windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetDBConnectionDialog extends JDialog {

    private static final int DIALOG_WIDTH = 300;

    private String dbName;
    private String dbPort;
    private String username;
    private String pass;
    private String hostName;
    private String hostPort;

    private boolean isCorrect = false;

    private JLabel lblDBName = new JLabel("Имя БД:");
    private JTextField tfDBName = new JTextField();
    private JLabel lblDBPort = new JLabel("Порт сервера БД:");
    private JTextField tfDBPort = new JTextField();
    private JLabel lblUsername = new JLabel("Имя пользователя:");
    private JTextField tfUserName = new JTextField();
    private JLabel lblPass = new JLabel("Пароль:");
    private JTextField tfPass = new JTextField();
    private JLabel lblHostName = new JLabel("Имя сервера приложения:");
    private JTextField tfHostName = new JTextField();
    private JLabel lblHostPort = new JLabel("Порт сервера приложения:");
    private JTextField tfHostPort = new JTextField();

    private JPanel pnlMain = new JPanel(new GridLayout(6, 2));
    private JPanel pnlButton = new JPanel();
    private JButton btnSave = new JButton("Сохранить");
    private JButton btnCancel = new JButton("Отмена");

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SetDBConnectionDialog(Frame owner) {
        super(owner, "Конфигурация БД", true);
    }

    public void showDialog() {
        init();
    }

    private void init() {
        pnlMain.add(lblDBName);
        pnlMain.add(tfDBName);
        pnlMain.add(lblDBPort);
        pnlMain.add(tfDBPort);
        pnlMain.add(lblUsername);
        pnlMain.add(tfUserName);
        pnlMain.add(lblPass);
        pnlMain.add(tfPass);
        pnlMain.add(lblHostName);
        pnlMain.add(tfHostName);
        pnlMain.add(lblHostPort);
        pnlMain.add(tfHostPort);

        getContentPane().add(pnlMain, BorderLayout.CENTER);

        BoxLayout boxLayout = new BoxLayout(pnlButton, BoxLayout.X_AXIS);
        pnlButton.setLayout(boxLayout);
        int strut = DIALOG_WIDTH - btnSave.getPreferredSize().width - btnCancel.getPreferredSize().width - 15;
        pnlButton.add(Box.createHorizontalStrut(strut));
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDbName(tfDBName.getText().trim());
                setDbPort(tfDBPort.getText().trim());
                setUsername(tfUserName.getText().trim());
                setPass(tfPass.getText().trim());
                setHostName(tfHostName.getText().trim());
                setHostPort(tfHostPort.getText().trim());
                if(isCorrect()) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                                getParent(),
                                "Введены не все данные.",
                                "Предупреждение",
                                JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        pnlButton.add(btnSave);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        pnlButton.add(btnCancel);

        getContentPane().add(pnlButton, BorderLayout.SOUTH);

        pack();
        setSize(DIALOG_WIDTH, getHeight());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public boolean isCorrect() {
        try {
            Integer.parseInt(dbPort);
            Integer.parseInt(hostPort);
        } catch (NumberFormatException e) {
            return false;
        }
        return ((dbName != null) &&
                (dbPort != null) &&
                (username != null) &&
                (pass != null) &&
                (hostName != null) &&
                (hostPort != null));
    }
}
