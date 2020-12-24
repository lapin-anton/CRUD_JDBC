import exceptions.IncorrectValueRangeException;
import product.*;

import java.io.IOException;
import java.util.ArrayList;

public class GuiReadCommand implements Command {

    private Result result;
    private QuerySet querySet;
    private ArrayList<Product> products;

    public GuiReadCommand(QuerySet querySet) {
        this.querySet = querySet;
    }

    @Override
    public void execute() {
        try {
            extractAllProductsByType(querySet.getProductType());
            switch (querySet.getCriteria()) {
                case BY_ID:
                    getProductById(Integer.parseInt(querySet.getCriteriaValue()));
                    break;
                case BY_MODEL:
                    getProductByModel(querySet.getCriteriaValue());
                    break;
                case BY_MAKER:
                    getProductByMaker(querySet.getCriteriaValue());
                    break;
                case BY_PRICE:
                    getProductByPrice(querySet.getMinValue(), querySet.getMaxValue());
                    break;
                case BY_SPEED:
                    getProductBySpeed(querySet.getMinValue(), querySet.getMaxValue());
                    break;
                case BY_HD:
                    getProductByHD(querySet.getMinValue(), querySet.getMaxValue());
                    break;
                case BY_RAM:
                    getProductByRam(querySet.getMinValue(), querySet.getMaxValue());
                    break;
                case BY_CD:
                    getProductByCD(querySet.getCriteriaValue());
                    break;
                case BY_SCREEN:
                    getProductByScreen(querySet.getMinValue(), querySet.getMaxValue());
                    break;
                case BY_PRINTING_TYPE:
                    getProductByPrintingType(querySet.getCriteriaValue());
                    break;
                case BY_COLOR:
                    getProductByColor(querySet.getCriteriaValue());
            }
            result.setProducts(products);
        } catch (IncorrectValueRangeException e1) {
            result = new Result(e1.getMessage());
        } catch (NumberFormatException | ClassNotFoundException e) {
            result = new Result("Ошибка! Неверный формат введенных Вами данных.");
        } catch (IOException e3) {
            result = new Result("Ошибка подключения. Проверьте соединение с сервером.");
        }
    }

    protected void extractAllProductsByType(ProductType type) throws IOException, ClassNotFoundException {
        Order order = new Order(type);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        result = connector.clientReceive();
        products = (ArrayList<Product>) result.getProductList();
    }

    private void getProductById(int id) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if(p.getId() == id)
                list.add(p);
        }
        products = list;
    }

    protected void getProductByModel(String model) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if(p.getModel().equals(model))
                list.add(p);
        }
        products = list;
    }

    protected void getProductByMaker(String maker) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if(p.getMaker().equals(maker))
                list.add(p);
        }
        products = list;
    }

    protected void getProductByPrice(String sMinPrice, String sMaxPrice) throws IncorrectValueRangeException {
        int minPrice = -1;
        int maxPrice = -1;
        if (sMinPrice.equals("")) minPrice = Integer.MIN_VALUE;
        if (sMaxPrice.equals("")) maxPrice = Integer.MAX_VALUE;
        if(minPrice == -1) minPrice = Integer.parseInt(sMinPrice);
        if(maxPrice == -1) maxPrice = Integer.parseInt(sMaxPrice);
        if(minPrice > maxPrice) throw new IncorrectValueRangeException();
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if((p.getPrice() >= minPrice) && (p.getPrice() <= maxPrice))
                list.add(p);
        }
        products = list;
    }

    private void getProductByColor(String color) {
        ArrayList<Product> list = new ArrayList<>();
        Printer printer = null;
        for (Product p: products) {
            printer = (Printer) p;
            if(printer.getColor().equals(color))
                list.add(p);
        }
        products = list;
    }

    private void getProductByPrintingType(String type) {
        ArrayList<Product> list = new ArrayList<>();
        Printer printer = null;
        for (Product p: products) {
            printer = (Printer) p;
            if(printer.getType().equals(type))
                list.add(p);
        }
        products = list;
    }

    private void getProductByScreen(String sMinScreen, String sMaxScreen) throws IncorrectValueRangeException {
        int minScreen = -1;
        int maxScreen = -1;
        if (sMinScreen.equals("")) minScreen = Integer.MIN_VALUE;
        if (sMaxScreen.equals("")) maxScreen = Integer.MAX_VALUE;
        if(minScreen == -1) minScreen = Integer.parseInt(sMinScreen);
        if(maxScreen == -1) maxScreen = Integer.parseInt(sMaxScreen);
        if(minScreen > maxScreen) throw new IncorrectValueRangeException();
        ArrayList<Product> list = new ArrayList<>();
        Laptop laptop = null;
        for (Product p: products) {
            laptop = (Laptop) p;
            if((laptop.getScreen() >= minScreen) && (laptop.getScreen() <= maxScreen))
                list.add(laptop);
        }
        products = list;
    }

    private void getProductByCD(String cd) {
        ArrayList<Product> list = new ArrayList<>();
        PC pc = null;
        for (Product p: products) {
            pc = (PC) p;
            if(pc.getCd().equals(cd))
                list.add(p);
        }
        products = list;
    }

    private void getProductByRam(String sMinRam, String sMaxRam) throws IncorrectValueRangeException {
        int minRam = -1;
        int maxRam = -1;
        if (sMinRam.equals("")) minRam = Integer.MIN_VALUE;
        if (sMaxRam.equals("")) maxRam = Integer.MAX_VALUE;
        if(minRam == -1) minRam = Integer.parseInt(sMinRam);
        if(maxRam == -1) maxRam = Integer.parseInt(sMaxRam);
        if(minRam > maxRam) throw new IncorrectValueRangeException();
        ArrayList<Product> list = new ArrayList<>();
        if(querySet.getProductType().equals(ProductType.PC)) {
            PC pc = null;
            for (Product p: products) {
                pc = (PC) p;
                if((pc.getRam() >= minRam) && (pc.getRam() <= maxRam))
                    list.add(pc);
            }
        } else {
            Laptop laptop = null;
            for (Product p: products) {
                laptop = (Laptop) p;
                if((laptop.getRam() >= minRam) && (laptop.getRam() <= maxRam))
                    list.add(laptop);
            }
        }
        products = list;
    }

    private void getProductByHD(String sMinHd, String sMaxHd) throws IncorrectValueRangeException {
        int minHd = -1;
        int maxHd = -1;
        if (sMinHd.equals("")) minHd = Integer.MIN_VALUE;
        if (sMaxHd.equals("")) maxHd = Integer.MAX_VALUE;
        if(minHd == -1) minHd = Integer.parseInt(sMinHd);
        if(maxHd == -1) maxHd = Integer.parseInt(sMaxHd);
        if(minHd > maxHd) throw new IncorrectValueRangeException();
        ArrayList<Product> list = new ArrayList<>();
        if(querySet.getProductType().equals(ProductType.PC)) {
            PC pc = null;
            for (Product p: products) {
                pc = (PC) p;
                if((pc.getHd() >= minHd) && (pc.getHd() <= maxHd))
                    list.add(pc);
            }
        } else {
            Laptop laptop = null;
            for (Product p: products) {
                laptop = (Laptop) p;
                if((laptop.getHd() >= minHd) && (laptop.getHd() <= maxHd))
                    list.add(laptop);
            }
        }
        products = list;
    }

    private void getProductBySpeed(String sMinSpeed, String sMaxSpeed) throws IncorrectValueRangeException {
        int minSpeed = -1;
        int maxSpeed = -1;
        if (sMinSpeed.equals("")) minSpeed = Integer.MIN_VALUE;
        if (sMaxSpeed.equals("")) maxSpeed = Integer.MAX_VALUE;
        if(minSpeed == -1) minSpeed = Integer.parseInt(sMinSpeed);
        if(maxSpeed == -1) maxSpeed = Integer.parseInt(sMaxSpeed);
        if(minSpeed > maxSpeed) throw new IncorrectValueRangeException();
        ArrayList<Product> list = new ArrayList<>();
        if(querySet.getProductType().equals(ProductType.PC)) {
            PC pc = null;
            for (Product p: products) {
                pc = (PC) p;
                if((pc.getSpeed() >= minSpeed) && (pc.getSpeed() <= maxSpeed))
                    list.add(pc);
            }
        } else {
            Laptop laptop = null;
            for (Product p: products) {
                laptop = (Laptop) p;
                if((laptop.getSpeed() >= minSpeed) && (laptop.getSpeed() <= maxSpeed))
                    list.add(laptop);
            }
        }
        products = list;
    }

    public Result getResult() {
        return result;
    }
}
