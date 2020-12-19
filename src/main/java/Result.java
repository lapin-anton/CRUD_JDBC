import product.Product;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {

    private ArrayList<Product> products;
    private String updateStatus; // результат добавления, удаления, обновления в БД

    public Result(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Result(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProductList() {
        return products;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }
}
