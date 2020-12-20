import product.ProductType;

import javax.swing.*;
import java.awt.*;

public class ClientGuiView {
    private final ClientGuiController controller;

    private JFrame frame = new JFrame("Компания оргтехники");
    // меню приложения
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("Файл");
    private JMenuItem newFile = new JMenuItem("Новый...");
    private JMenuItem save = new JMenuItem("Сохранить");
    private JMenuItem saveAs = new JMenuItem("Сохранить как...");
    private JMenuItem exit = new JMenuItem("Выход");
    private JMenu editMenu = new JMenu("Редактировать");
    private JMenu settingsMenu = new JMenu("Настройки");
    private JMenu helpMenu = new JMenu("Помощь");
    private JMenuItem about = new JMenuItem("О программе");
    // панель с кнопками действий
    // боковая панель фильтров поиска
    private JPanel filterPanel = new JPanel();
    private JLabel productTypeLbl = new JLabel("Выберите тип товара:");
    private JComboBox<String> productTypes = new JComboBox();
    private DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<>();
    private JTextArea criteria = new JTextArea();
    private JTextArea additionalCriteria = new JTextArea();
    // таблица для результатов поиска и редактирования
    private JTable table = new JTable();

//    private JPanel southPane = new JPanel();
//    private JTextField textField = new JTextField(50);
//    private JTextArea messages = new JTextArea(10, 50);
//    private JTextArea users = new JTextArea(10, 10);
//    private JButton send = new JButton("Отправить");

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        fileMenu.add(newFile);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(exit);

        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);
        // добавляем главное меню
        frame.setJMenuBar(menuBar);
        // добавляем таблицу
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        // добавляем боковую панель с фильтрами
        filterPanel.setBackground(Color.GRAY);
        filterPanel.setLayout(new GridLayout(15, 1));
        filterPanel.add(productTypeLbl);
        cbModel.addElement("-не выбрано-");
        cbModel.addElement("ПК");
        cbModel.addElement("Ноутбуки");
        cbModel.addElement("Принтеры");
        productTypes.setModel(cbModel);
        productTypes.setSelectedIndex(0);
        filterPanel.add(productTypes);
        criteria.setVisible(false);
        filterPanel.add(criteria);
        additionalCriteria.setVisible(false);
        filterPanel.add(additionalCriteria);
        frame.getContentPane().add(filterPanel, BorderLayout.WEST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {

    }

    public void refreshTable() {
        // обновление таблицы...
    }

//    class SendAction implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            controller.sendTextMessage(textField.getText());
//            textField.setText("");
//        }
//    }


}
