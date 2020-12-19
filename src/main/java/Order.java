import product.Product;
import product.ProductType;

import java.io.Serializable;

public class Order implements Serializable {
    private CommandType commandType;
    private ProductType productType;
    private String model;
    private Product product;

    //Запрос на чтение
    public Order(ProductType productType) {
        this.commandType = CommandType.READ;
        this.productType = productType;
    }

    // Запрос на чтение одной записи
    public Order(CommandType commandType, ProductType productType, String model) {
        this.commandType = commandType;
        this.productType = productType;
        this.model = model;
    }

    // запрос на удаление
    public Order(ProductType productType, String model) {
        this.commandType = CommandType.DELETE;
        this.productType = productType;
        this.model = model;
    }

    // Запрос на добавление
    public Order(ProductType productType, Product product) {
        this.commandType = CommandType.CREATE;
        this.productType = productType;
        this.product = product;
    }

    // запрос на обновление
    public Order(ProductType productType, String model, Product product) {
        this.commandType = CommandType.UPDATE;
        this.productType = productType;
        this.model = model;
        this.product = product;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String getModel() {
        return model;
    }

    public Product getProduct() {
        return product;
    }
}
