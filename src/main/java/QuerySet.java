import product.Product;
import product.ProductType;

import java.util.ArrayList;

public class QuerySet {
    // модель для запроса на чтение
    private ProductType productType;
    private Criteria criteria;
    private String criteriaValue;
    private int minPriceValue;
    private int maxPriceValue;

    // модель для запроса на добавление
    private Product product;
    // модель для запроса на удаление
    private String productModel;
    private int id;
    private ArrayList<String> productModels; // множественное удаление

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

    public int getMinPriceValue() {
        return minPriceValue;
    }

    public void setMinPriceValue(int minPriceValue) {
        this.minPriceValue = minPriceValue;
    }

    public int getMaxPriceValue() {
        return maxPriceValue;
    }

    public void setMaxPriceValue(int maxPriceValue) {
        this.maxPriceValue = maxPriceValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
