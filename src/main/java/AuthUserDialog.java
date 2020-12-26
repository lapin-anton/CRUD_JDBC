import layouts.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthUserDialog extends JFrame {

    private static final int WIDTH = 250;
    private static final int HEIGHT = 180;

    private ClientGuiController controller;
    private String login;
    private String password;

    private JPanel pnlButton = new JPanel();
    private JPanel pnlMain = new JPanel(new VerticalLayout());
    private JLabel lblLogin = new JLabel("Логин:");
    private JLabel lblPassword = new JLabel("Пароль:");
    private JTextField tfLogin = new JTextField();
    private JTextField tfPassword = new JTextField();
    private JButton btnSubmit = new JButton("Войти");
    private JButton btnDemo = new JButton("Демо");

    private Font labelFont = new Font("TimesNewRoman", Font.BOLD, 20),
            buttonFont = new Font("TimesNewRoman", Font.BOLD, 16);


    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AuthUserDialog(ClientGuiController controller) throws HeadlessException {
        this.controller = controller;
        init();
    }

    private void init() {
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        lblLogin.setFont(labelFont);
        pnlMain.add(lblLogin);
        tfLogin.setFont(buttonFont);
        pnlMain.add(tfLogin);
        lblPassword.setFont(labelFont);
        pnlMain.add(lblPassword);
        tfPassword.setFont(buttonFont);
        pnlMain.add(tfPassword);

        pnlButton.setLayout(new BoxLayout(pnlButton, BoxLayout.X_AXIS));
        int strut = WIDTH - btnSubmit.getPreferredSize().width - btnDemo.getPreferredSize().width - 50;
        pnlButton.add(Box.createHorizontalStrut(strut));
        btnSubmit.setFont(buttonFont);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login = tfLogin.getText().trim();
                password = tfPassword.getText().trim();
                if (login.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Введены не все данные", "Информация",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    controller.initAuth();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(getParent(), "Ошибка! Проверьте подключение с сервером.",
                            "Ошибка работы приложения",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDemo.setFont(buttonFont);
        pnlButton.add(btnSubmit);
        btnDemo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.initDemo();
            }
        });
        pnlButton.add(btnDemo);


        getContentPane().add(pnlMain);
        getContentPane().add(pnlButton, BorderLayout.SOUTH);

        pack();
        setTitle("Авторизация");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
