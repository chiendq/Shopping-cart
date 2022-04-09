package hanu.a2_1901040037.db;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040037.models.Product;

public class ProductCursorWrapper extends CursorWrapper {

    private Cursor cursor;

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
        this.cursor = cursor;
    }

    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();

        // for each row in cursor
        while (!cursor.isLast()) {
            Product product = getProduct();
            if (product == null) return productList;
            // add note into notes
            productList.add(product);
        }
        return productList;
    }

    public Product getProduct() {
        cursor.moveToNext();

        int idIndex = cursor.getColumnIndex("id");
        int thumbnailIndex = cursor.getColumnIndex("thumbnail");
        int nameIndex = cursor.getColumnIndex("name");
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");
        int quantityIndex = cursor.getColumnIndex("quantity");
        Product product;
        // process row
        try {
            int id = cursor.getInt(idIndex);
            String thumbnail = cursor.getString(thumbnailIndex);
            String name = cursor.getString(nameIndex);
            int unitPrice = cursor.getInt(unitPriceIndex);
            int quantity = cursor.getInt(quantityIndex);
            // create ProductSelected object
            product = new Product(id, thumbnail, name, unitPrice, quantity);
        } catch (CursorIndexOutOfBoundsException e) {
            return null;
        }

        return product;
    }

}
