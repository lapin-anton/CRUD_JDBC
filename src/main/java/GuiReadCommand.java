import product.Product;
import product.ProductType;

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
                case BY_MODEL: getProductByModel(querySet.getCriteriaValue());
                    break;
                case BY_MAKER: getProductByMaker(querySet.getCriteriaValue());
                    break;
                case BY_PRICE: getProductByPrice(querySet.getMinPriceValue(), querySet.getMaxPriceValue());
            }
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
        }
    }

    protected void extractAllProductsByType(ProductType type) throws IOException, ClassNotFoundException {
        Order order = new Order(type);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        result = connector.clientReceive();
        products = (ArrayList<Product>) result.getProductList();
    }

    protected void getProductByModel(String model) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if(p.getModel().equals(model))
                list.add(p);
        }
        result.setProducts(list);
    }

    protected void getProductByMaker(String maker) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if(p.getMaker().equals(maker))
                list.add(p);
        }
        result.setProducts(list);
    }

    protected void getProductByPrice(int minPrice, int maxPrice) {
        ArrayList<Product> list = new ArrayList<>();
        for (Product p: products) {
            if((p.getPrice() >= minPrice) && (p.getPrice() <= maxPrice))
                list.add(p);
        }
        result.setProducts(list);
    }

    public Result getResult() {
        return result;
    }
}
