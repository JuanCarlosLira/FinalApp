package afinal.app.com.finalapp;

import android.provider.BaseColumns;

public class ProductEntry implements BaseColumns {
    public static final String TABLE_NAME     = "product";
    public static final String COLUMN_ID      = "id";
    public static final String COLUMN_NAME    = "name";
    public static final String COLUMN_MODEL   = "model";
    public static final String COLUMN_DESC    = "description";
    public static final String COLUMN_PRICE   = "price";

    private int id;
    private String name;
    private String model;
    private String description;
    private int price;

    public ProductEntry() {
    }

    public ProductEntry(String name, String model, String description, int price) {
        this.name = name;
        this.model = model;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
