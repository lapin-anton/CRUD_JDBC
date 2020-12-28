import product.Product;
import product.ProductType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {
    private CommandType commandType;
    private ProductType productType;
    private String model;
    private ArrayList<String> models;
    private Product product;
    private HashMap<String, Product> products;
    private User user;

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
    // запрос на множественное удаление
    public Order(ProductType productType, ArrayList<String> models) {
        this.commandType = CommandType.MULTIPLE_DELETE;
        this.productType = productType;
        this.models = models;
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
    // запрос на множественное обновление
    public Order(ProductType productType, HashMap<String, Product> products) {
        this.commandType = CommandType.MULTIPLE_UPDATE;
        this.productType = productType;
        this.products = products;
    }

    // запрос на авторизацию
    public Order(User user) {
        this.commandType = CommandType.AUTHORIZATION;
        this.user = user;
    }

    // добавление нового пользователя
    public Order(CommandType commandType, User user) {
        this.commandType = commandType;
        this.user = user;
    }

    // чтение всех пользователей


    public Order(CommandType commandType) {
        this.commandType = commandType;
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

    public ArrayList<String> getModels() {
        return models;
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public User getUser() {
        return user;
    }
}
