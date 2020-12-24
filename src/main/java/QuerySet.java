import product.Product;
import product.ProductType;

import java.util.ArrayList;
import java.util.HashMap;

public class QuerySet {
    // модель для запроса на чтение
    private ProductType productType;
    private Criteria criteria;
    private String criteriaValue;
    private String minValue;
    private String maxValue;

    // модель для запроса на добавление
    private Product product;
    // модель для запроса на удаление
    private String productModel;
    private int id;
    private ArrayList<String> productModels; // множественное удаление
    // модель для запроса на обновление
    private HashMap<String, Product> products;

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Product> products) {
        this.products = products;
    }

    public ArrayList<String> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<String> productModels) {
        this.productModels = productModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public String getCriteriaValue() {
        return criteriaValue;
    }

    public void setCriteriaValue(String criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minPriceValue) {
        this.minValue = minPriceValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
