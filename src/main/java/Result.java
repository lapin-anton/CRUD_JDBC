import product.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Result implements Serializable {

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

    public List<Product> getProductList() {
        return products;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
