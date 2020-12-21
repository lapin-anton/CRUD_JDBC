import product.Product;
import product.ProductType;

import java.io.IOException;
import java.util.ArrayList;

public class GuiReadCommand extends ReadCommand {

    private ClientGuiModel guiModel;

    public void execute(QuerySet set) {
        try {
            extractAllProductsByType(set.getProductType());
            switch (set.getCriteria()) {
                case BY_MODEL: getProductByModel(set.getCriteriaValue());
                    break;
                case BY_MAKER: getProductByMaker(set.getCriteriaValue());
                    break;
                case BY_PRICE: getProductByPrice(set.getMinPriceValue(), set.getMaxPriceValue());
            }
            guiModel = new ClientGuiModel();
            guiModel.setNewResult(result);
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

    public ClientGuiModel getModel() {
        return guiModel;
    }
}
