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
}
