import product.*;
import start_windows.SetDBConnectionDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ClientGuiView {

    private static final int COLUMN_WIDTH = 120;
    //типы товаров для выпадающего списка
    private final String[] PRODUCT_NAMES = {Constants.PC_NAME, Constants.LAPTOP_NAME, Constants.PRINTER_NAME};
    // наименования колонок таблиц с результатами запросов
    private final Object[] PC_COLUMNS = new String[]{Constants.ID, Constants.MODEL, Constants.MAKER,
            Constants.PRICE, Constants.SPEED, Constants.HD, Constants.RAM, Constants.CD};
    private final Object[] LAPTOP_COLUMNS = new String[]{Constants.ID, Constants.MODEL, Constants.MAKER,
            Constants.PRICE, Constants.SPEED, Constants.HD, Constants.RAM, Constants.SCREEN};
    private final Object[] PRINTER_COLUMNS = new String[]{Constants.ID, Constants.MODEL, Constants.MAKER,
            Constants.PRICE, Constants.PRINTING_TYPE, Constants.COLOR};
    private final Object[] EMPTY_TABLE_COLUMN = new String[]{Constants.EMPTY_LIST_COLUMN_INFO};
    private final Object[][] EMPTY_TABLE_DATA = new Object[][]{{Constants.INVITE_TO_SEARCH}};
    private static final ArrayList<String> MIN_MAX_VALUE_LIST = new ArrayList<>();
    static {
        MIN_MAX_VALUE_LIST.add(Constants.PRICE);
        MIN_MAX_VALUE_LIST.add(Constants.SPEED);
        MIN_MAX_VALUE_LIST.add(Constants.HD);
        MIN_MAX_VALUE_LIST.add(Constants.RAM);
        MIN_MAX_VALUE_LIST.add(Constants.SCREEN);
    }
    private static final int[] PC_CRITERIA = {Criteria.NONE.ordinal(), Criteria.BY_ID.ordinal(), Criteria.BY_MODEL.ordinal(),
        Criteria.BY_MAKER.ordinal(), Criteria.BY_PRICE.ordinal(), Criteria.BY_SPEED.ordinal(), Criteria.BY_HD.ordinal(),
        Criteria.BY_RAM.ordinal(), Criteria.BY_CD.ordinal()};
    private static final int[] LAPTOP_CRITERIA = {Criteria.NONE.ordinal(), Criteria.BY_ID.ordinal(), Criteria.BY_MODEL.ordinal(),
        Criteria.BY_MAKER.ordinal(), Criteria.BY_PRICE.ordinal(), Criteria.BY_SPEED.ordinal(), Criteria.BY_HD.ordinal(),
        Criteria.BY_RAM.ordinal(), Criteria.BY_SCREEN.ordinal()};
    private static final int[] PRINTER_CRITERIA = {Criteria.NONE.ordinal(), Criteria.BY_ID.ordinal(), Criteria.BY_MODEL.ordinal(),
        Criteria.BY_MAKER.ordinal(), Criteria.BY_PRICE.ordinal(), Criteria.BY_PRINTING_TYPE.ordinal(),
        Criteria.BY_COLOR.ordinal()};

    // картинки на кнопках на панели инструментов
    private static final String CREATE_ICON = "./src/main/resources/images/icon32x32/create.png";
    private static final String UPDATE_ICON = "./src/main/resources/images/icon32x32/update.png";
    private static final String DELETE_ICON = "./src/main/resources/images/icon32x32/delete.png";

    private final ClientGuiController controller;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JFrame frame = new JFrame(Constants.COMPANY_TITLE);
    // меню приложения
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu(Constants.MENU_FILE);
    private JMenuItem newFile = new JMenuItem(Constants.MENU_NEW);
    private JMenuItem save = new JMenuItem(Constants.MENU_SAVE);
    private JMenuItem saveAs = new JMenuItem(Constants.MENU_SAVE_AS);
    private JMenuItem exit = new JMenuItem(Constants.MENU_EXIT);
    private JMenu actionMenu = new JMenu(Constants.MENU_ACTIONS);
    private JMenuItem createProductMI = new JMenuItem(Constants.MENU_CREATE);
    private JMenuItem updateProductMI = new JMenuItem(Constants.MENU_UPDATE);
    private JMenuItem deleteProductMI = new JMenuItem(Constants.MENU_DELETE);
    private JMenu settingsMenu = new JMenu(Constants.MENU_SETTINGS);
    private JMenuItem authNewMI = new JMenuItem(Constants.MENU_AUTH_NEW);
    private JMenuItem authChangeMI = new JMenuItem(Constants.MENU_AUTH_CHANGE);
    private JMenuItem connectionMI = new JMenuItem(Constants.MENU_CONNECTION);
    private JMenu helpMenu = new JMenu(Constants.MENU_HELP);
    private JMenuItem about = new JMenuItem(Constants.MENU_ABOUT);
    // панель с кнопками действий
    private JToolBar toolBar = new JToolBar();
    private JPanel toolPanel;
    // боковая панель фильтров поиска
    private JPanel filterPanel = new JPanel(new VerticalLayout());
    private JLabel productTypeLbl = new JLabel(Constants.OPTION_PRODUCT_TYPE);
    private JComboBox<String> productTypes = new JComboBox();
    private DefaultComboBoxModel<String> cbProductModel = new DefaultComboBoxModel<>();
    private JLabel criteriaLbl = new JLabel(Constants.OPTION_CRITERIA);
    private JComboBox<String> criteria = new JComboBox<>();
    private DefaultComboBoxModel<String> cbCriteriaModel = new DefaultComboBoxModel<>();
    private ButtonGroup rBtnGroup = new ButtonGroup();
    private JRadioButton minMaxRBtn = new JRadioButton(Constants.RANGE_VALUE_RADIO);
    private JRadioButton minRBtn = new JRadioButton(Constants.MIN_VALUE_RADIO);
    private JRadioButton maxRBtn = new JRadioButton(Constants.MAX_VALUE_RADIO);
    private JLabel criteria_1lbl = new JLabel();
    private JTextField criteriaVal_1 = new JTextField(20);
    private JLabel criteria_2lbl = new JLabel();
    private JTextField criteriaVal_2 = new JTextField(20);
    private JButton searchBtn = new JButton(Constants.SEARCH);
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
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        fileMenu.add(exit);

        createProductMI.addActionListener(new CreateAction());
        actionMenu.add(createProductMI);
        updateProductMI.addActionListener(new UpdateAction());
        actionMenu.add(updateProductMI);
        deleteProductMI.addActionListener(new DeleteAction());
        actionMenu.add(deleteProductMI);

        authNewMI.addActionListener(new SetAuthNewActionListener());
        settingsMenu.add(authNewMI);
        authChangeMI.addActionListener(new SetAuthChangeActionListener());
        settingsMenu.add(authChangeMI);
        connectionMI.addActionListener(new SetConnectActionListener());
        settingsMenu.add(connectionMI);

        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(actionMenu);
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
        toolPanel.add(Box.createHorizontalStrut(5));
        //добавляем панель инструментов
        frame.getContentPane().add(toolPanel, BorderLayout.NORTH);

        // добавляем боковую панель с фильтрами
        filterPanel.setBackground(Color.GRAY);
        filterPanel.add(productTypeLbl);
        cbProductModel.addElement(Constants.NOT_SELECTED);
        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            cbProductModel.addElement(PRODUCT_NAMES[i]);
        }
        productTypes.setModel(cbProductModel);
        productTypes.setSelectedIndex(0);
        productTypes.addActionListener(new SelectProductListener());
        filterPanel.add(productTypes);

        criteriaLbl.setVisible(false);
        filterPanel.add(criteriaLbl);
        cbCriteriaModel.addElement(Constants.NOT_SELECTED);
        criteria.setModel(cbCriteriaModel);
        criteria.setSelectedIndex(0);
        criteria.addActionListener(new SelectCriteriaListener());
        criteria.setVisible(false);
        filterPanel.add(criteria);
        // группа опций "цены"
        minMaxRBtn.setVisible(false);
        minRBtn.setVisible(false);
        maxRBtn.setVisible(false);
        minMaxRBtn.addActionListener(new MinMaxPriceListener());
        rBtnGroup.add(minMaxRBtn);
        minRBtn.addActionListener(new MinPriceListener());
        rBtnGroup.add(minRBtn);
        maxRBtn.addActionListener(new MaxPriceListener());
        rBtnGroup.add(maxRBtn);
        filterPanel.add(minMaxRBtn);
        filterPanel.add(minRBtn);
        filterPanel.add(maxRBtn);

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
        Dimension foolScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize (foolScreenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        if(Client.getSettings().isNotExists()) {
            SetDBConnectionDialog dialog = new SetDBConnectionDialog(frame);
            dialog.showDialog();
            try {
                new Settings(dialog.getDbName(), dialog.getDbPort(), dialog.getUsername(), dialog.getPass(),
                        Integer.parseInt(dialog.getHostPort()), dialog.getHostName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        controller.clientConnected = clientConnected;
    }

    // обновляем таблицу
    public void refreshTable() {
        ArrayList<Product> products = (ArrayList<Product>) controller.getModel().getNewResult().getProductList();
        ProductType[] types = ProductType.values();
        Object[][] data = Constants.EMPTY_LIST;
        Object[] columnNames = Constants.DEFAULT_COLUMN;
        if ((products != null) && (!products.isEmpty())) {
            switch (types[productTypes.getSelectedIndex()]) {
                case PC:
                    columnNames = PC_COLUMNS;
                    data = fillPCData(products);
                    break;
                case LAPTOP:
                    columnNames = LAPTOP_COLUMNS;
                    data = fillLaptopData(products);
                    break;
                case PRINTER:
                    columnNames = PRINTER_COLUMNS;
                    data = fillPrinterData(products);
            }
        } else {
            String message = controller.getModel().getNewResult().getUpdateStatus();
            if(products == null) data[0][0] = message;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        // добавляем таблицу
        table.setModel(tableModel);
    }

    private Object[][] fillPCData(ArrayList<Product> products) {
        PC pc = null;
        Object[][] data = new Object[products.size()][PC_COLUMNS.length];
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
        Object[][] data = new Object[products.size()][LAPTOP_COLUMNS.length];
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
        Object[][] data = new Object[products.size()][PRINTER_COLUMNS.length];
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
            resetAllParameters();
            clearTable();
            int index = productTypes.getSelectedIndex();
            if (index > 0) {
                criteriaLbl.setVisible(true);
                criteria.setSelectedIndex(0);
                criteria.setVisible(true);
                searchBtn.setEnabled(true);
                fillCbCriteriaByProductType(index);
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

        private void fillCbCriteriaByProductType(int index) {
            cbCriteriaModel.removeAllElements();
            cbCriteriaModel.addElement(Constants.NOT_SELECTED);
            ProductType[] types = ProductType.values();
            switch (types[index]) {
                case PC: fillPCCriteria();
                    break;
                case LAPTOP: fillLaptopCriteria();
                    break;
                case PRINTER: fillPrinterCriteria();
            }
        }

        private void fillPrinterCriteria() {
            for (int i = 0; i < PRINTER_COLUMNS.length; i++) {
                cbCriteriaModel.addElement((String) PRINTER_COLUMNS[i]);
            }
        }

        private void fillLaptopCriteria() {
            for (int i = 0; i < LAPTOP_COLUMNS.length; i++) {
                cbCriteriaModel.addElement((String) LAPTOP_COLUMNS[i]);
            }
        }

        private void fillPCCriteria() {
            for (int i = 0; i < PC_COLUMNS.length; i++) {
                cbCriteriaModel.addElement((String) PC_COLUMNS[i]);
            }
        }
    }

    private void resetAllParameters() {
        criteria_1lbl.setVisible(false);
        criteriaVal_1.setVisible(false);
        criteria_2lbl.setVisible(false);
        criteriaVal_2.setVisible(false);
        rBtnGroup.clearSelection();
        minMaxRBtn.setVisible(false);
        minRBtn.setVisible(false);
        maxRBtn.setVisible(false);
    }

    private class SelectCriteriaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetAllParameters();
            clearAllCriteriaValues();
            int index = criteria.getSelectedIndex();
            if(index < 0) return;
            String curr_item = criteria.getItemAt(index);
            if (index > 0) {
                criteria_1lbl.setVisible(true);
                criteriaVal_1.setVisible(true);
                if(MIN_MAX_VALUE_LIST.contains(curr_item)) {
                    doMinMaxValuesVisible(curr_item);
                } else {
                    switch (curr_item) {
                        case Constants.ID: criteria_1lbl.setText(Constants.OPTION_INPUT + Constants.ID_NAME);
                            break;
                        case Constants.MODEL: criteria_1lbl.setText(Constants.OPTION_INPUT + Constants.MODEL_NAME);
                            break;
                        case Constants.MAKER: criteria_1lbl.setText(Constants.OPTION_INPUT + Constants.MAKER_NAME);
                            break;
                        case Constants.CD: criteria_1lbl.setText(Constants.OPTION_INPUT + Constants.CD_NAME);
                            break;
                        case Constants.PRINTING_TYPE: criteria_1lbl.setText(Constants.OPTION_INPUT +
                                Constants.PRINTING_TYPE_NAME);
                            break;
                        case Constants.COLOR: criteria_1lbl.setText(Constants.OPTION_INPUT + Constants.COLOR_NAME);
                    }
                }
            }
        }

        private void clearAllCriteriaValues() {
            criteriaVal_1.setText("");
            criteriaVal_2.setText("");
        }

        private void doMinMaxValuesVisible(String item) {
            String minValueType = "";
            String maxValueType = "";
            switch (item) {
                case Constants.PRICE:
                    minValueType = Constants.MIN_PRICE;
                    maxValueType = Constants.MAX_PRICE;
                    break;
                case Constants.SPEED:
                    minValueType = Constants.MIN_SPEED;
                    maxValueType = Constants.MAX_SPEED;
                    break;
                case Constants.HD:
                    minValueType = Constants.MIN_HD;
                    maxValueType = Constants.MAX_HD;
                    break;
                case Constants.RAM:
                    minValueType = Constants.MIN_RAM;
                    maxValueType = Constants.MAX_RAM;
                    break;
                case Constants.SCREEN:
                    minValueType = Constants.MIN_SCREEN;
                    maxValueType = Constants.MAX_SCREEN;
                    break;
            }
            minMaxRBtn.setVisible(true);
            minRBtn.setVisible(true);
            maxRBtn.setVisible(true);
            criteria_1lbl.setText(Constants.OPTION_INPUT + minValueType);
            criteriaVal_1.setEnabled(false);
            criteria_2lbl.setText(Constants.OPTION_INPUT + maxValueType);
            criteria_2lbl.setVisible(true);
            criteriaVal_2.setVisible(true);
            criteriaVal_2.setEnabled(false);
        }
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearTable();
            int prod_type = productTypes.getSelectedIndex();
            int criteria_index = criteria.getSelectedIndex();
            ProductType[] types = ProductType.values();
            Criteria[] criteria = Criteria.values();
            QuerySet querySet = new QuerySet();
            querySet.setProductType(types[prod_type]);
            int ordinal = -1;
            switch (types[prod_type]) {
                case PC: ordinal = PC_CRITERIA[criteria_index];
                    break;
                case LAPTOP: ordinal = LAPTOP_CRITERIA[criteria_index];
                    break;
                case PRINTER: ordinal = PRINTER_CRITERIA[criteria_index];
            }
            querySet.setCriteria(criteria[ordinal]);
            switch (criteria[ordinal]) {
                case BY_PRICE:
                case BY_HD:
                case BY_RAM:
                case BY_SPEED:
                case BY_SCREEN:
                    querySet.setMinValue(criteriaVal_1.getText());
                    querySet.setMaxValue(criteriaVal_2.getText());
                    break;
                default:
                    querySet.setCriteriaValue(criteriaVal_1.getText());
            }
            controller.sendQuery(CommandType.READ, querySet);
        }
    }

    private void clearTable() {
        table.setModel(new DefaultTableModel(EMPTY_TABLE_DATA, EMPTY_TABLE_COLUMN));
        frame.repaint();
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
                    new ImageIcon(CREATE_ICON), PRODUCT_NAMES, PRODUCT_NAMES[0]);
            int prod_type = 0;
            if(selected != null) {
                String s = (String) selected;
                for (int i = 0; i < PRODUCT_NAMES.length; i++) {
                    if(s.equals(PRODUCT_NAMES[i])) {
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
                case PC: columnNames = PC_COLUMNS;
                    prodName = "ПК";
                    break;
                case LAPTOP: columnNames = LAPTOP_COLUMNS;
                    prodName = "ноутбука";
                    break;
                case PRINTER: columnNames = PRINTER_COLUMNS;
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
            JButton okBtn = new JButton("Добавить");
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
            JButton cancelBtn = new JButton("Отмена");
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            table.setEditingRow(0);
            dialog.getContentPane().add(new JScrollPane(table), BorderLayout.NORTH);
            int tableWidth = table.getColumnCount() * COLUMN_WIDTH;
            int strut = tableWidth - okBtn.getPreferredSize().width - cancelBtn.getPreferredSize().width - 20;
            btnPanel.add(Box.createHorizontalStrut(strut));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setSize(tableWidth, dialog.getHeight());
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
                case PC: columnNames = PC_COLUMNS;
                    break;
                case LAPTOP: columnNames = LAPTOP_COLUMNS;
                    break;
                case PRINTER: columnNames = PRINTER_COLUMNS;
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
            int tableWidth = updTable.getColumnCount() * COLUMN_WIDTH;
            int strut = tableWidth - okBtn.getPreferredSize().width - cancelBtn.getPreferredSize().width - 20;
            btnPanel.add(Box.createHorizontalStrut(strut));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setSize(tableWidth, dialog.getHeight());
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

    private class MinMaxPriceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(minMaxRBtn.isSelected()) {
                criteriaVal_1.setEnabled(true);
                criteriaVal_2.setEnabled(true);
            }
        }
    }

    private class MinPriceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(minRBtn.isSelected()) {
                criteriaVal_1.setEnabled(true);
                criteriaVal_2.setEnabled(false);
                criteriaVal_2.setText("");
            }
        }
    }

    private class MaxPriceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(maxRBtn.isSelected()) {
                criteriaVal_1.setEnabled(false);
                criteriaVal_1.setText("");
                criteriaVal_2.setEnabled(true);
            }
        }
    }

    private class SetConnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showSetConnectionDialog();
//            try {
//                Settings settings = new Settings();
//                String username = JOptionPane.showInputDialog(
//                        frame,
//                        "Введите имя пользователя БД:",
//                        "Конфигурация сервера",
//                        JOptionPane.QUESTION_MESSAGE);
//                if(username != null) settings.setUsername(username);
//                String password = JOptionPane.showInputDialog(
//                        frame,
//                        "Введите пароль для подключения к БД:",
//                        "Конфигурация сервера",
//                        JOptionPane.QUESTION_MESSAGE);
//                if(username != null) settings.setPassword(password);
//                String host = JOptionPane.showInputDialog(
//                        frame,
//                        "Введите имя хоста сервера:",
//                        "Конфигурация клиента",
//                        JOptionPane.QUESTION_MESSAGE);
//                if (host != null) settings.setHostName(host);
//                while (true) {
//                    String sPort = JOptionPane.showInputDialog(
//                            frame,
//                            "Введите порт сервера:",
//                            "Конфигурация клиента",
//                            JOptionPane.QUESTION_MESSAGE);
//                    try {
//                        int port = Integer.parseInt(sPort.trim());
//                        settings.setPort(port);
//                        break;
//                    } catch (NumberFormatException ex) {
//                        JOptionPane.showMessageDialog(
//                                frame,
//                                "Был введен некорректный порт сервера. Попробуйте еще раз.",
//                                "Конфигурация клиента",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            } catch (Exception exception) {
//                ExceptionHandler.log(exception);
//            }
        }

        private void showSetConnectionDialog() {
            try {
                Settings settings = new Settings();
                String userName = settings.getUsername();
                String password = settings.getPassword();
                String hostName = settings.getHostName();
                int port = settings.getPort();
                JPanel mainPnl = new JPanel(new VerticalLayout());
                JDialog dialog = new JDialog(frame, "Изменение настроек подключения к серверу и БД", true);
                JTextField tfUserName = new JTextField(20);
                tfUserName.setText(userName);
                JTextField tfPassword = new JTextField(20);
                tfPassword.setText(password);
                JTextField tfHostName = new JTextField(20);
                tfHostName.setText(hostName);
                JTextField tfPort = new JTextField(20);
                tfPort.setText(String.valueOf(port));
                JLabel lClientHeader = new JLabel("Конфигурация клиента");
                JLabel lDBHeader = new JLabel("Конфигурация подключения к БД");
                JLabel lUserName = new JLabel(Constants.OPTION_INPUT + Constants.USER_NAME);
                JLabel lPassword = new JLabel(Constants.OPTION_INPUT + Constants.PASSWORD);
                JLabel lHostName = new JLabel(Constants.OPTION_INPUT + Constants.HOST_NAME);
                JLabel lPort = new JLabel(Constants.OPTION_INPUT + Constants.PORT);
                JPanel buttonPnl = new BoxLayoutUtils().createHorizontalPanel();
                JButton saveBtn = new JButton("Сохранить");
                saveBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        settings.setUsername(tfUserName.getText().trim());
                        settings.setPassword(tfPassword.getText().trim());
                        settings.setHostName(tfHostName.getText().trim());
                        try {
                            int port = Integer.parseInt(tfPort.getText().trim());
                            settings.saveNewSettings();
                            settings.setPort(port);
                            dialog.dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Был введен некорректный порт сервера. Попробуйте еще раз.",
                                "Конфигурация клиента",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                JButton cancelBtn = new JButton("Отмена");
                cancelBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                separator.setSize(separator.getWidth(), 1);

                mainPnl.add(lClientHeader);
                mainPnl.add(lHostName);
                mainPnl.add(tfHostName);
                mainPnl.add(lPort);
                mainPnl.add(tfPort);
                mainPnl.add(Box.createVerticalStrut(5));
                mainPnl.add(lDBHeader);
                mainPnl.add(lUserName);
                mainPnl.add(tfUserName);
                mainPnl.add(lPassword);
                mainPnl.add(tfPassword);

                dialog.getContentPane().add(mainPnl);
                int dialogWidth = 210;
                int strut = dialogWidth - saveBtn.getPreferredSize().width - cancelBtn.getPreferredSize().width - 15;
                buttonPnl.add(Box.createHorizontalStrut(strut));
                buttonPnl.add(saveBtn);
                buttonPnl.add(cancelBtn);

                dialog.getContentPane().add(buttonPnl, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setSize(dialogWidth, 300);
                dialog.setResizable(false);
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SetAuthNewActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


        }
    }

    private class SetAuthChangeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {

            } catch (Exception exception) {
                ExceptionHandler.log(exception);
            }
        }
    }
}
