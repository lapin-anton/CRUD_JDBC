import product.Product;
import product.ProductType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Result implements Serializable {

    private ProductType productType;
    private List<Product> products;
    private String updateStatus; // результат добавления, удаления, обновления в БД
    private boolean isUserExists;
    private User user;
    private Object[][] data;

    public Result(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Result(ArrayList<Product> products) {
        this.products = products;
    }

    public Result(Product product, String updateStatus) {
        this.products = Collections.singletonList(product);
        this.updateStatus = updateStatus;
    }

    public Result(ProductType productType, List<Product> products) {
        this.productType = productType;
        this.products = products;
    }

    public Result(boolean isUserExists, User user) {
        this.isUserExists = isUserExists;
        this.user = user;
    }

    public Result(Object[][] data) {
        this.data = data;
    }

    public List<Product> getProductList() {
        return products;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public boolean isUserExists() {
        return isUserExists;
    }

    public User getUser() {
        return user;
    }

    public Object[][] getData() {
        return data;
    }
}
