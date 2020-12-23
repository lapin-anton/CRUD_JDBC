import product.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ClientGuiView {
    private final ClientGuiController controller;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String NOT_SELECTED = "-не выбрано-";
    //типы товаров для выпадающего списка
    private final String[] cbProdTypesNames = {"ПК", "Ноутбуки", "Принтеры"};
    //критерии поиска для выпадающего списка
    private final String[] cbCategoryNames = {"по модели", "по компании", "по цене"};
    // наименования колонок таблиц с результатами запросов
    private final Object[] PCColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Скорость", "Память ЖД",
            "Оперативная память", "Скорость CD-привода"};
    private final Object[] LaptopColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Скорость", "Память ЖД",
            "Оперативная память", "Размер экрана"};
    private final Object[] PrinterColumnNames = new String[]{"id", "Модель", "Производитель", "Цена", "Тип печати",
            "Цветная печать"};
    private final Object[][] emptyList = {{"По Вашему запросу ничего не найдено"}};
    private final Object[] defaultListColumn = {"Результат обработки запроса"};
    // картинки на кнопках на панели инструментов
    private static final String CREATE_ICON = "./src/main/resources/images/create.png";
    private static final String UPDATE_ICON = "./src/main/resources/images/update.png";
    private static final String DELETE_ICON = "./src/main/resources/images/delete.png";

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
    private JToolBar toolBar = new JToolBar();
    private JPanel toolPanel;
    // боковая панель фильтров поиска
    private JPanel filterPanel = new JPanel(new VerticalLayout());
    private JLabel productTypeLbl = new JLabel("Выберите тип товара:");
    private JComboBox<String> productTypes = new JComboBox();
    private DefaultComboBoxModel<String> cbProductModel = new DefaultComboBoxModel<>();
    private JLabel criteriaLbl = new JLabel("Выберите критерий поиска:");
    private JComboBox<String> criteria = new JComboBox<>();
    private DefaultComboBoxModel<String> cbCriteriaModel = new DefaultComboBoxModel<>();
    private JLabel criteria_1lbl = new JLabel();
    private JTextField criteriaVal_1 = new JTextField(20);
    private JLabel criteria_2lbl = new JLabel();
    private JTextField criteriaVal_2 = new JTextField(20);
    private JButton searchBtn = new JButton("Найти");
    // таблица для результатов поиска и редактирования
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

        toolBar.add((Action) new CreateAction());
        toolBar.add((Action) new UpdateAction());
        toolBar.add((Action) new DeleteAction());
        toolBar.setFloatable(false);
        // Панель с горизонтальным расположением компонентов
        BoxLayoutUtils blUtils = new BoxLayoutUtils();
        toolPanel = blUtils.createHorizontalPanel();
        toolPanel.add(toolBar);
        toolPanel.add(Box.createHorizontalStrut(32));
        //добавляем панель инструментов
        frame.getContentPane().add(toolPanel, BorderLayout.NORTH);

        // добавляем боковую панель с фильтрами
        filterPanel.setBackground(Color.GRAY);
        filterPanel.add(productTypeLbl);
        cbProductModel.addElement(NOT_SELECTED);
        for (int i = 0; i < cbProdTypesNames.length; i++) {
            cbProductModel.addElement(cbProdTypesNames[i]);
        }
        productTypes.setModel(cbProductModel);
        productTypes.setSelectedIndex(0);
        productTypes.addActionListener(new SelectProductListener());
        filterPanel.add(productTypes);

        criteriaLbl.setVisible(false);
        filterPanel.add(criteriaLbl);
        cbCriteriaModel.addElement(NOT_SELECTED);
        for (int i = 0; i < cbCategoryNames.length; i++) {
            cbCriteriaModel.addElement(cbCategoryNames[i]);
        }
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
        // добавляем таблицу
        frame.getContentPane().add(new JScrollPane(table));
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
        Object[][] data = emptyList;
        Object[] columnNames = defaultListColumn;
        if ((products != null) && (!products.isEmpty())) {
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
            if(products == null) data[0][0] = message;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        // добавляем таблицу
        table.setModel(tableModel);
        frame.setSize(filterPanel.getWidth() + table.getColumnCount() * 120, frame.getHeight());
        frame.repaint();
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
                case BY_PRICE: querySet.setMinPriceValue(Integer.parseInt(criteriaVal_1.getText()));
                    querySet.setMaxPriceValue(Integer.parseInt(criteriaVal_2.getText()));
            }
            controller.sendQuery(CommandType.READ, querySet);
        }
    }

    private class CreateAction extends Component implements Action {

        private HashMap<String, Object> values = new HashMap<>();

        public CreateAction() {
            putValue(AbstractAction.SMALL_ICON, new ImageIcon(CREATE_ICON));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ProductType[] types = ProductType.values();
            Object selected = JOptionPane.showInputDialog(
                    frame,
                    "Выберите тип товара, который необходимо добавить:",
                    "Выбор типа товара",
                    JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon(CREATE_ICON), cbProdTypesNames, cbProdTypesNames[0]);
            int prod_type = 0;
            if(selected != null) {
                String s = (String) selected;
                for (int i = 0; i < cbProdTypesNames.length; i++) {
                    if(s.equals(cbProdTypesNames[i])) {
                        prod_type = ++i;
                        break;
                    }
                }
                showCreateProductDialog(types[prod_type]);
            }
        }

        private void showCreateProductDialog(ProductType type) {
            final Object[] columnNames;
            String prodName = null;
            switch (type) {
                case PC: columnNames = PCColumnNames;
                    prodName = "ПК";
                    break;
                case LAPTOP: columnNames = LaptopColumnNames;
                    prodName = "ноутбука";
                    break;
                case PRINTER: columnNames = PrinterColumnNames;
                    prodName = "принтера";
                    break;
                default:
                    columnNames = new Object[0];
                    prodName = "продукта";
            }
            Object[][] data = new Object[1][columnNames.length];
            JDialog dialog = new JDialog(frame, String.format("Добавление %s в базу", prodName), true);
            JTable table = new JTable(data, columnNames);
            JPanel btnPanel = new BoxLayoutUtils().createHorizontalPanel();
            JButton okBtn = new JButton("ОК");
            okBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isFullEdited = true;
                    Object[][] newData = new Object[1][columnNames.length];
                    for (int i = 0; i < newData[0].length; i++) {
                        newData[0][i] = table.getValueAt(0, i);
                        if(newData[0][i] == null) {
                            isFullEdited = false;
                            break;
                        }
                    }
                    if(isFullEdited) {
                        Product product = null;
                        QuerySet set = new QuerySet();
                        switch (type) {
                            case PC: product = saveNewPC(newData);
                                break;
                            case LAPTOP: product = saveNewLaptop(newData);
                                break;
                            case PRINTER: product = saveNewPrinter(newData);
                        }
                        set.setProductType(type);
                        set.setProduct(product);
                        controller.sendQuery(CommandType.CREATE, set);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Заполнены не все ячейки таблицы",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            table.setEditingRow(0);
            dialog.getContentPane().add(new JScrollPane(table), BorderLayout.NORTH);
            btnPanel.add(Box.createHorizontalStrut(50));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }

        private Product saveNewPrinter(Object[][] data) {
            String model = (String) data[0][1];
            String maker = (String) data[0][2];
            int price = Integer.parseInt((String) data[0][3]);
            String type = (String) data[0][4];
            String color = (String) data[0][5];
            return new Printer(0, model, maker, price, type, color);
        }

        private Product saveNewLaptop(Object[][] data) {
            String model = (String) data[0][1];
            String maker = (String) data[0][2];
            int price = Integer.parseInt((String) data[0][3]);
            int speed = Integer.parseInt((String) data[0][4]);
            int hd = Integer.parseInt((String) data[0][5]);
            int ram = Integer.parseInt((String) data[0][6]);
            int screen = Integer.parseInt((String) data[0][7]);
            return new Laptop(0, model, maker, price, speed, hd, ram, screen);
        }

        private PC saveNewPC(Object[][] data) {
            String model = (String) data[0][1];
            String maker = (String) data[0][2];
            int price = Integer.parseInt((String) data[0][3]);
            int speed = Integer.parseInt((String) data[0][4]);
            int hd = Integer.parseInt((String) data[0][5]);
            int ram = Integer.parseInt((String) data[0][6]);
            String cd = (String) data[0][7];
            return new PC(0, model, maker, price, speed, hd, ram, cd);
        }

        @Override
        public Object getValue(String key) {
            return this.values.get(key);
        }

        @Override
        public void putValue(String key, Object value) {
            this.values.put(key, value);
        }
    }

    private class UpdateAction extends Component implements Action {

        private HashMap<String, Object> values = new HashMap<>();

        public UpdateAction() {
            putValue(AbstractAction.SMALL_ICON, new ImageIcon(UPDATE_ICON));
        }

        @Override
        public Object getValue(String key) {
            return this.values.get(key);
        }

        @Override
        public void putValue(String key, Object value) {
            this.values.put(key, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Integer> selectedRows = new ArrayList<>();
            for(int i = 0; i < table.getRowCount(); i++) {
                if (table.isRowSelected(i)) {
                    selectedRows.add(i);
                }
            }
            if(selectedRows.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Не выбран ни один товар из списка",
                        "Изменение данных о товарах", JOptionPane.WARNING_MESSAGE);
            } else {
                showUpdateProductDialog(selectedRows);
            }
        }

        private void showUpdateProductDialog(ArrayList<Integer> selectedRows) {
            ProductType[] types = ProductType.values();
            final Object[] columnNames;
            final Object[][] data;
            final Object[][] backUpData;
            switch (types[productTypes.getSelectedIndex()]) {
                case PC: columnNames = PCColumnNames;
                    break;
                case LAPTOP: columnNames = LaptopColumnNames;
                    break;
                case PRINTER: columnNames = PrinterColumnNames;
                    break;
                default: columnNames = new Object[0];
            }
            data = new Object[selectedRows.size()][columnNames.length];
            backUpData = new Object[selectedRows.size()][columnNames.length];
            for (int i = 0; i < selectedRows.size(); i++) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = table.getValueAt(selectedRows.get(i), j);
                    backUpData[i][j] = table.getValueAt(selectedRows.get(i), j);
                }
            }
            JDialog dialog = new JDialog(frame, "Обновление данных товара в базе", true);
            JTable updTable = new JTable(data, columnNames);
            for (int i = 0; i < data.length; i++) {
                updTable.setEditingRow(i);
            }
            JPanel btnPanel = new BoxLayoutUtils().createHorizontalPanel();
            JButton okBtn = new JButton("Обновить");
            okBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isEdited = false;
                    Object[][] newData = new Object[selectedRows.size()][columnNames.length];
                    for (int i = 0; i < newData.length; i++) {
                        for (int j = 0; j < newData[i].length; j++) {
                            newData[i][j] = String.valueOf(updTable.getValueAt(i, j));
                            if(!newData[i][j].equals(String.valueOf(backUpData[i][j]))) {
                                isEdited = true;
                            }
                        }
                    }
                    if(isEdited) {
                        HashMap<String, Product> map = new HashMap<>();
                        QuerySet set = new QuerySet();
                        switch (types[productTypes.getSelectedIndex()]) {
                            case PC: map = getUpdPCs(backUpData, newData);
                                break;
                            case LAPTOP: map = getUpdLaptops(backUpData, newData);
                                break;
                            case PRINTER: map = getUpdPrinters(backUpData, newData);
                        }
                        set.setProductType(types[productTypes.getSelectedIndex()]);
                        set.setProducts(map);
                        controller.sendQuery(CommandType.UPDATE, set);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Не было изменено ни одного параметра товаров",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            dialog.getContentPane().add(new JScrollPane(updTable), BorderLayout.NORTH);
            btnPanel.add(Box.createHorizontalStrut(50));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }

        private HashMap<String, Product> getUpdPrinters(Object[][] backUpData, Object[][] newData) {
            HashMap<String, Product> result = new HashMap<>();
            for (int i = 0; i < newData.length; i++) {
                String model = (String) newData[i][1];
                String maker = (String) newData[i][2];
                int price = Integer.parseInt((String) newData[i][3]);
                String type = (String) newData[i][4];
                String color = (String) newData[i][5];
                result.put((String) backUpData[i][1], new Printer(0, model, maker, price, type, color));
            }
            return result;
        }

        private HashMap<String, Product> getUpdLaptops(Object[][] backUpData, Object[][] newData) {
            HashMap<String, Product> result = new HashMap<>();
            for (int i = 0; i < newData.length; i++) {
                String model = (String) newData[i][1];
                String maker = (String) newData[i][2];
                int price = Integer.parseInt((String) newData[i][3]);
                int speed = Integer.parseInt((String) newData[i][4]);
                int hd = Integer.parseInt((String) newData[i][5]);
                int ram = Integer.parseInt((String) newData[i][6]);
                int screen = Integer.parseInt((String) newData[i][7]);
                result.put((String) backUpData[i][1], new Laptop(0, model, maker, price, speed, hd, ram, screen));
            }
            return result;
        }

        private HashMap<String, Product> getUpdPCs(Object[][] backUpData, Object[][] newData) {
            HashMap<String, Product> result = new HashMap<>();
            for (int i = 0; i < newData.length; i++) {
                String model = (String) newData[i][1];
                String maker = (String) newData[i][2];
                int price = Integer.parseInt((String) newData[i][3]);
                int speed = Integer.parseInt((String) newData[i][4]);
                int hd = Integer.parseInt((String) newData[i][5]);
                int ram = Integer.parseInt((String) newData[i][6]);
                String cd = (String) newData[i][7];
                result.put((String) backUpData[i][1], new PC(0, model, maker, price, speed, hd, ram, cd));
            }
            return result;
        }
    }

    private class DeleteAction extends Component implements Action {

        private HashMap<String, Object> values = new HashMap<>();

        public DeleteAction() {
            putValue(AbstractAction.SMALL_ICON, new ImageIcon(DELETE_ICON));
        }

        @Override
        public Object getValue(String key) {
            return this.values.get(key);
        }

        @Override
        public void putValue(String key, Object value) {
            this.values.put(key, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Integer> selectedRows = new ArrayList<>();
            for(int i = 0; i < table.getRowCount(); i++) {
                if (table.isRowSelected(i)) {
                    selectedRows.add(i);
                }
            }
            if(selectedRows.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Не выбран ни один товар для удаления из списка",
                        "Удаление товаров", JOptionPane.WARNING_MESSAGE);
            } else {
                showDeleteProductDialog(selectedRows);
            }
        }

        private void showDeleteProductDialog(ArrayList<Integer> selectedRows) {
            ArrayList<String> delModels = new ArrayList<>();
            ProductType[] types = ProductType.values();
            String[] out = new String[selectedRows.size() + 1];
            out[0] = String.format("Удалить выбранные модели %s?", productTypes.getItemAt(productTypes.getSelectedIndex()));
            int i = 1;
            for (Integer row: selectedRows) {
                out[i] = (String) table.getValueAt(row, 1);
                ++i;
            }
            int answer = JOptionPane.showConfirmDialog(frame, out, "Удаление товаров", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if(answer == 0) {
                for (int j = 1; j < out.length; j++) {
                    delModels.add(out[j]);
                }
                QuerySet set = new QuerySet();
                set.setProductType(types[productTypes.getSelectedIndex()]);
                set.setProductModels(delModels);
                controller.sendQuery(CommandType.DELETE, set);
            }
        }
    }

    class BoxLayoutUtils {
        // Выравнивание компонентов по оси X
        public void setGroupAlignmentX(JComponent[] cs, float alignment) {
            for (int i = 0; i < cs.length; i++) {
                cs[i].setAlignmentX(alignment);
            }
        }
        // Выравнивание компонентов по оси Y
        public void setGroupAlignmentY(JComponent[] cs, float alignment) {
            for (int i = 0; i < cs.length; i++) {
                cs[i].setAlignmentY(alignment);
            }
        }
        // Создание панели с установленным вертикальным блочным расположением
        public JPanel createVerticalPanel() {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            return p;
        }
        // Создание панели с установленным горизонтальным блочным расположением
        public JPanel createHorizontalPanel() {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
            return p;
        }
    }

    class VerticalLayout implements LayoutManager
    {
        private Dimension size = new Dimension();

        // Следующие два метода не используются
        public void addLayoutComponent   (String name, Component comp) {}
        public void removeLayoutComponent(Component comp) {}

        // Метод определения минимального размера для контейнера
        public Dimension minimumLayoutSize(Container c) {
            return calculateBestSize(c);
        }
        // Метод определения предпочтительного размера для контейнера
        public Dimension preferredLayoutSize(Container c) {
            return calculateBestSize(c);
        }
        // Метод расположения компонентов в контейнере
        public void layoutContainer(Container container)
        {
            // Список компонентов
            Component list[] = container.getComponents();
            int currentY = 5;
            for (int i = 0; i < list.length; i++) {
                // Определение предпочтительного размера компонента
                Dimension pref = list[i].getPreferredSize();
                // Размещение компонента на экране
                list[i].setBounds(5, currentY, pref.width, pref.height);
                // Учитываем промежуток в 5 пикселов
                currentY += 5;
                // Смещаем вертикальную позицию компонента
                currentY += pref.height;
            }
        }
        // Метод вычисления оптимального размера контейнера
        private Dimension calculateBestSize(Container c)
        {
            // Вычисление длины контейнера
            Component[] list = c.getComponents();
            int maxWidth = 0;
            for (int i = 0; i < list.length; i++) {
                int width = list[i].getWidth();
                // Поиск компонента с максимальной длиной
                if ( width > maxWidth )
                    maxWidth = width;
            }
            // Размер контейнера в длину с учетом левого и правого отступа
            size.width = maxWidth + 10;
            // Вычисление высоты контейнера
            int height = 0;
            for (int i = 0; i < list.length; i++) {
                height += 5;
                height += list[i].getHeight();
            }
            size.height = height;
            return size;
        }
    }
}
