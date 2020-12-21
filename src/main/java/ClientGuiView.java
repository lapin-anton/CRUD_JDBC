import product.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class ClientGuiView {
    private final ClientGuiController controller;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object[] PCColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Скорость", "Память ЖД",
            "Оперативная память", "Скорость CD-привода"};
    Object[] LaptopColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Скорость", "Память ЖД",
            "Оперативная память", "Размер экрана"};
    Object[] PrinterColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Тип печати",
            "Цветная печать"};

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
    private DefaultComboBoxModel<String> cbProductModel = new DefaultComboBoxModel<>();
    private JLabel criteriaLbl = new JLabel("Выберите критерий поиска:");
    private JComboBox<String> criteria = new JComboBox<>();
    private DefaultComboBoxModel<String> cbCriteriaModel = new DefaultComboBoxModel<>();
    private JLabel criteria_1lbl = new JLabel();
    private JTextField criteriaVal_1 = new JTextField();
    private JLabel criteria_2lbl = new JLabel();
    private JTextField criteriaVal_2 = new JTextField();
    private JButton searchBtn = new JButton("Найти");
    // таблица для результатов поиска и редактирования
    private JPanel canvas = new JPanel();
    private JTable table = new JTable();

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

        // добавляем боковую панель с фильтрами
        filterPanel.setBackground(Color.GRAY);
        filterPanel.setLayout(new GridLayout(15, 1));
        filterPanel.add(productTypeLbl);
        cbProductModel.addElement("-не выбрано-");
        cbProductModel.addElement("ПК");
        cbProductModel.addElement("Ноутбуки");
        cbProductModel.addElement("Принтеры");
        productTypes.setModel(cbProductModel);
        productTypes.setSelectedIndex(0);
        productTypes.addActionListener(new SelectProductListener());
        filterPanel.add(productTypes);

        criteriaLbl.setVisible(false);
        filterPanel.add(criteriaLbl);
        cbCriteriaModel.addElement("-не выбрано-");
        cbCriteriaModel.addElement("по модели");
        cbCriteriaModel.addElement("по производителю");
        cbCriteriaModel.addElement("по цене");
        criteria.setModel(cbCriteriaModel);
        criteria.setSelectedIndex(0);
        criteria.addActionListener(new SelectCriteriaListener());
        criteria.setVisible(false);
        filterPanel.add(criteria);

        criteria_1lbl.setVisible(false);
        filterPanel.add(criteria_1lbl);
        criteriaVal_1.setVisible(false);
        filterPanel.add(criteriaVal_1);
        criteria_2lbl.setVisible(false);
        filterPanel.add(criteria_2lbl);
        criteriaVal_2.setVisible(false);
        filterPanel.add(criteriaVal_2);

        searchBtn.addActionListener(new SearchListener());
        searchBtn.setEnabled(false);
        filterPanel.add(searchBtn);

        frame.getContentPane().add(filterPanel, BorderLayout.WEST);

        canvas.add(new JScrollPane(table));
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {

    }
    // обновляем таблицу
    public void refreshTable() {
        ArrayList<Product> products = (ArrayList<Product>) controller.getModel().getNewResult().getProductList();
        ProductType[] types = ProductType.values();
        Object[][] data = {{"Список пуст"}};
        Object[] columnNames = {"Результат запроса"};
        if (products != null) {
            switch (types[productTypes.getSelectedIndex()]) {
                case PC:
                    columnNames = PCColumnNames;
                    data = fillPCData(products);
                    break;
                case LAPTOP:
                    columnNames = LaptopColumnNames;
                    data = fillLaptopData(products);
                    break;
                case PRINTER:
                    columnNames = PrinterColumnNames;
                    data = fillPrinterData(products);
            }
        } else {
            String message = controller.getModel().getNewResult().getUpdateStatus();
            data[0][0] = message;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        // добавляем таблицу
        table.setModel(tableModel);
        canvas.repaint();
    }

    private Object[][] fillPCData(ArrayList<Product> products) {
        PC pc = null;
        Object[][] data = new Object[products.size()][PCColumnNames.length];
        for (int i = 0; i < data.length; i++) {
            pc = (PC) products.get(i);
            data[i][0] = pc.getId();
            data[i][1] = pc.getModel();
            data[i][2] = pc.getMaker();
            data[i][3] = pc.getPrice();
            data[i][4] = pc.getSpeed();
            data[i][5] = pc.getHd();
            data[i][6] = pc.getRam();
            data[i][7] = pc.getCd();
        }
        return data;
    }

    private Object[][] fillLaptopData(ArrayList<Product> products) {
        Laptop laptop = null;
        Object[][] data = new Object[products.size()][LaptopColumnNames.length];
        for (int i = 0; i < data.length; i++) {
            laptop = (Laptop) products.get(i);
            data[i][0] = laptop.getId();
            data[i][1] = laptop.getModel();
            data[i][2] = laptop.getMaker();
            data[i][3] = laptop.getPrice();
            data[i][4] = laptop.getSpeed();
            data[i][5] = laptop.getHd();
            data[i][6] = laptop.getRam();
            data[i][7] = laptop.getScreen();
        }
        return data;
    }

    private Object[][] fillPrinterData(ArrayList<Product> products) {
        Printer printer = null;
        Object[][] data = new Object[products.size()][PrinterColumnNames.length];
        for (int i = 0; i < data.length; i++) {
            printer = (Printer) products.get(i);
            data[i][0] = printer.getId();
            data[i][1] = printer.getModel();
            data[i][2] = printer.getMaker();
            data[i][3] = printer.getPrice();
            data[i][4] = printer.getType();
            data[i][5] = printer.getColor();
        }
        return data;
    }

    private class SelectProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = productTypes.getSelectedIndex();
            if (index > 0) {
                criteriaLbl.setVisible(true);
                criteria.setSelectedIndex(0);
                criteria.setVisible(true);
                searchBtn.setEnabled(true);
            } else {
                criteriaLbl.setVisible(false);
                criteria.setVisible(false);
                criteria_1lbl.setVisible(false);
                criteriaVal_1.setVisible(false);
                criteria_2lbl.setVisible(false);
                criteriaVal_2.setVisible(false);
                searchBtn.setEnabled(false);
            }
        }
    }

    private class SelectCriteriaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int prod_type = productTypes.getSelectedIndex();
            int index = criteria.getSelectedIndex();
            switch (index) {
                case 1: criteria_1lbl.setText("Введите название модели:");
                    break;
                case 2: criteria_1lbl.setText("Введите название производителя:");
                    break;
                case 3: criteria_1lbl.setText("Введите минимальную цену:");
                        criteria_2lbl.setText("Введите максимальную цену:");
            }
            if (index > 0) {
                criteria_1lbl.setVisible(true);
                criteriaVal_1.setVisible(true);
                criteria_2lbl.setVisible(false);
                criteriaVal_2.setVisible(false);
                if (index == 3) {
                    criteria_2lbl.setVisible(true);
                    criteriaVal_2.setVisible(true);
                }
            } else {
                criteria_1lbl.setVisible(false);
                criteriaVal_1.setVisible(false);
                criteria_2lbl.setVisible(false);
                criteriaVal_2.setVisible(false);
            }
        }
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int prod_type = productTypes.getSelectedIndex();
            int criteria_index = criteria.getSelectedIndex();
            ProductType[] types = ProductType.values();
            Criteria[] criteria = Criteria.values();
            QuerySet querySet = new QuerySet();
            querySet.setProductType(types[prod_type]);
            querySet.setCriteria(criteria[criteria_index]);
            switch (criteria[criteria_index]) {
                case BY_MODEL:
                case BY_MAKER: querySet.setCriteriaValue(criteriaVal_1.getText());
                    break;
                case BY_PRICE: querySet.setCriteriaValue(criteriaVal_1.getText());
                    querySet.setCriteriaValue(criteriaVal_2.getText());
            }
            controller.sendSearchQuery(querySet);
        }
    }

}
