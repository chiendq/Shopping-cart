package hanu.a2_1901040037.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import hanu.a2_1901040037.models.Constant;
import hanu.a2_1901040037.models.Product;

public class ProductManager {
    private final SQLiteDatabase db;

    private static ProductManager instance;

    private ProductManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static ProductManager getInstance(Context context){
        if(instance == null){
            instance = new ProductManager(context);
        }
        return instance;
    }

    public Product getById(int id){
        // query db
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = " + id, null);
        cursor.moveToFirst();
        if(cursor.getColumnCount() > 0){
            int thumbnailIndex = cursor.getColumnIndex("thumbnail");
            int nameIndex = cursor.getColumnIndex("name");
            int unitPriceIndex = cursor.getColumnIndex("unitPrice");
            int quantityIndex = cursor.getColumnIndex("quantity");

            String thumbnail = cursor.getString(thumbnailIndex);
            String name = cursor.getString(nameIndex);
            int unitPrice = cursor.getInt(unitPriceIndex);
            int quantity = cursor.getInt(quantityIndex);
            return new Product(id, thumbnail,name,unitPrice,quantity);
        }
        return null;
    }

    public List<Product> all(){

        // query db
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constant.DB_TABLE_NAME, null);

        ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
        Log.d("ProductCursorWrapper","Select all Product");

        return  productCursorWrapper.getProducts();
    }

    // add a new product
    public void add(Product product){
        ContentValues values = new ContentValues();
        values.put(Constant.DB_FIELD_ID, product.getId());
        values.put(Constant.DB_FIELD_THUMBNAIL, product.getThumbnail());
        values.put(Constant.DB_FIELD_NAME, product.getName());
        values.put(Constant.DB_FIELD_UNIT_PRICE, product.getUnitPrice());
        values.put(Constant.DB_FIELD_QUANTITY, 1);

        Log.d("NoteManager","added Product");
        db.insert(Constant.DB_TABLE_NAME, null, values);
    }

    // add a new productList
    public void addAll(List<Product> productList){
        productList.forEach(product -> {
            ContentValues values = new ContentValues();
            values.put(Constant.DB_FIELD_ID, product.getId());
            values.put(Constant.DB_FIELD_THUMBNAIL, product.getThumbnail());
            values.put(Constant.DB_FIELD_NAME, product.getName());
            values.put(Constant.DB_FIELD_UNIT_PRICE, product.getUnitPrice());
            values.put(Constant.DB_FIELD_QUANTITY, 0);
            db.insert(Constant.DB_TABLE_NAME,null,values);
        });

        Log.d("NoteManager","added Product List" +productList);
    }

    public void addQuantity(Product product){
        int id = product.getId();
        Product productUpdate = getById(id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.DB_FIELD_ID, productUpdate.getId());
        contentValues.put(Constant.DB_FIELD_THUMBNAIL, productUpdate.getThumbnail());
        contentValues.put(Constant.DB_FIELD_NAME, productUpdate.getName());
        contentValues.put(Constant.DB_FIELD_UNIT_PRICE, productUpdate.getUnitPrice());
        contentValues.put(Constant.DB_FIELD_QUANTITY, productUpdate.getQuantity() + 1);
        db.update(Constant.DB_TABLE_NAME, contentValues,Constant.ID + " =" + id,null);
        Log.d("ProductManager","Add quantity " + getById(product.getId()));

    }

    public void minusQuantity(Product product){
        int id = product.getId();
        Product productUpdate = getById(id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.DB_FIELD_ID, productUpdate.getId());
        contentValues.put(Constant.DB_FIELD_THUMBNAIL, productUpdate.getThumbnail());
        contentValues.put(Constant.DB_FIELD_NAME, productUpdate.getName());
        contentValues.put(Constant.DB_FIELD_UNIT_PRICE, productUpdate.getUnitPrice());
        contentValues.put(Constant.DB_FIELD_QUANTITY, productUpdate.getQuantity() -1);
        db.update(Constant.DB_TABLE_NAME, contentValues,Constant.ID + " =" + id,null);
        Log.d("ProductManager","Minus quantity " + getById(product.getId()));
    }

    // delete all record
    public void clear(){
        db.delete(Constant.DB_TABLE_NAME, "1", null);
    }

    public boolean isEmpty(){
        Cursor cursor = db.rawQuery("SELECT * FROM products" , null);
        return cursor.moveToFirst();
    }

    public boolean isExistById(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = " + id, null);
        return cursor.moveToFirst();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        // close connection
        db.close();
    }
}
