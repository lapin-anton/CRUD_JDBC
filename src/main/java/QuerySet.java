import product.ProductType;

public class QuerySet {
    // модель для запроса на чтение
    private ProductType productType;
    private Criteria criteria;
    private String criteriaValue;
    private int minPriceValue;
    private int maxPriceValue;
    //
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
}
