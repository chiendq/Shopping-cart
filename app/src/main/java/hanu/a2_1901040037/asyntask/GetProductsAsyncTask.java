package hanu.a2_1901040037.asyntask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_1901040037.R;
import hanu.a2_1901040037.adapter.ProductAdapter;
import hanu.a2_1901040037.db.ProductManager;
import hanu.a2_1901040037.models.Constant;
import hanu.a2_1901040037.models.Product;

public class GetProductsAsyncTask extends AsyncTask<Void, Integer, Void> {

    private InputStream inputStream;

    private InputStreamReader inputStreamReader;

    private JsonReader jsonReader;

    private final Activity contextParent;

    private List<Product> productList;

    private ProductAdapter productAdapter;


    public GetProductsAsyncTask(Activity contextParent) {
        this.contextParent = contextParent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        productList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL cartApiEndpoint = new URL(Constant.PRODUCT_LIST_API);
            // open connection to the URL
            HttpsURLConnection APIConnection = (HttpsURLConnection) cartApiEndpoint.openConnection();

            // check status of connection
            if (APIConnection.getResponseCode() == 200) {
                // get input stream from connection
                inputStream = APIConnection.getInputStream();
                // read stream
                inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                // use JsonReader
                jsonReader = new JsonReader(inputStreamReader);
                // '[' at begin -> begin is a array
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    // next is '{' -> next is object
                    jsonReader.beginObject();

                    // id
                    jsonReader.nextName();
                    int id = jsonReader.nextInt();
                    // thumbnail link
                    jsonReader.nextName();
                    String thumbnail = jsonReader.nextString();
                    // name
                    jsonReader.nextName();
                    String name = jsonReader.nextString();
                    // uniPrice
                    jsonReader.nextName();
                    int unitPrice = jsonReader.nextInt();

                    productList.add(new Product(id, thumbnail, name, unitPrice));
                    // '}' is close bracket -> end of object
                    jsonReader.endObject();
                }
            }

            // check if database is the same as API provided
            // if not -> clear all record
            compareAPIvsDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                jsonReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void compareAPIvsDatabase(){
        ProductManager manager = ProductManager.getInstance(contextParent);
        List<Product> dbList = manager.all();
        for (int i = 0; i < productList.size(); i++) {
            if (!manager.isEmpty()) {
                manager.clear();
                manager.addAll(productList);
                break;
            }
            Product dbProduct = dbList.get(i);
            Product listProduct = productList.get(i);
            if ((dbProduct.getId() != listProduct.getId())
                    || !dbProduct.getThumbnail().equals(listProduct.getThumbnail())
                    || !dbProduct.getName().equals(listProduct.getName())
                    || (dbProduct.getUnitPrice() != listProduct.getUnitPrice())) {
                manager.clear();
                manager.addAll(productList);
                break;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    // this method will be called when progress finished.
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(contextParent, "Welcome", Toast.LENGTH_SHORT).show();

        //get ref to recycle view
        RecyclerView recyclerViewFriends = contextParent.findViewById(R.id.recyclerViewProduct);
        // Create layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(contextParent, 2);

        // set layout manager
        recyclerViewFriends.setLayoutManager(gridLayoutManager);

        // init adapter
        productAdapter = new ProductAdapter(ProductManager.getInstance(contextParent).all());

        // bind recyclerview with adapter
        recyclerViewFriends.setAdapter(productAdapter);

        // get ref of search view
        SearchView searchView = contextParent.findViewById(R.id.searchViewBar);

        // listen for action
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // filter product on search icon clicked
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            // filter product on text change
            @Override
            public boolean onQueryTextChange(String query) {
                filter(query);
                return false;
            }
        });
    }

    // filter as query
    private void filter(String query) {
        // creating a new array list to filter our data.
        List<Product> filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (Product item : productList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(contextParent, "No product found", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            productAdapter.filterList(filteredList);
        }
    }
}
