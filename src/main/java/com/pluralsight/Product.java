package com.pluralsight;

public class Product {
    //sku, product name, price, department
    private String sku;
    private String productName;
    private Double price;
    private String department;

    public Product(String sku, String productName, Double price, String department) {
        this.sku = sku;
        this.productName = productName;
        this.price = price;
        this.department = department;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", department='" + department + '\'' +
                '}';
    }
}
