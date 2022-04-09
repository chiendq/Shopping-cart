package hanu.a2_1901040037;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040037.adapter.MyCardAdapter;
import hanu.a2_1901040037.adapter.ProductAdapter;

import hanu.a2_1901040037.db.ProductManager;
import hanu.a2_1901040037.models.Product;

public class MyCartActivity extends AppCompatActivity {
    private MyCardAdapter myCardAdapter ;

    private ProductManager productManager;

    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        // get ref to recycler view
        RecyclerView recyclerViewMyCart =  findViewById(R.id.recyclerViewMyCart);

        // set layout for items
        recyclerViewMyCart.setLayoutManager(new LinearLayoutManager(MyCartActivity.this));

        // prepare dataset
        List<Product> products = new ArrayList<>();
        for (Product p: ProductManager.getInstance(this).all()) {
            if(p.getQuantity() > 0 ){
                products.add(p);
            }
        }
        productManager = ProductManager.getInstance(MyCartActivity.this);
        productList = products;

        if(productList.size() == 0){
            new AlertDialog.Builder(this)
                    .setTitle("Empty")
                    .setMessage("You have been not selected product ! Shopping now !")
                    .show();
        }
        Log.i("MyCardActivity", String.valueOf(productList));

        // init adapter
        myCardAdapter = new MyCardAdapter(productList);

        // set adapter
        recyclerViewMyCart.setAdapter(myCardAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}