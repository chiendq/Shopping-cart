package hanu.a2_1901040037.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040037.R;
import hanu.a2_1901040037.db.ProductManager;
import hanu.a2_1901040037.models.BitMapService;
import hanu.a2_1901040037.models.Product;


public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyCardHolder> {

    private List<Product> productList;

    public MyCardAdapter(List<Product> productList) {
        this.productList = productList;

    }

    private ProductManager productManager;

    protected class MyCardHolder extends RecyclerView.ViewHolder {
        ImageView imgViewThumbnail;

        TextView txtViewProductName_cart;
        TextView txtViewUnitPrice;
        TextView txtViewQuantity;
        TextView txtViewSumPrice_cart;
        TextView txtViewTotalPrice;

        ImageButton imgButtonPlus;
        ImageButton imgButtonMinus;

        public MyCardHolder(@NonNull View itemView) {
            super(itemView);

            imgViewThumbnail = itemView.findViewById(R.id.imgViewThumbnail);

            txtViewProductName_cart = itemView.findViewById(R.id.txtViewProductName_cart);
            txtViewUnitPrice = itemView.findViewById(R.id.txtViewUnitPrice);
            txtViewQuantity = itemView.findViewById(R.id.txtViewQuantity);
            txtViewSumPrice_cart = itemView.findViewById(R.id.txtViewSumPrice_cart);

            imgButtonPlus = itemView.findViewById(R.id.imgButtonPlus);
            imgButtonMinus = itemView.findViewById(R.id.imgButtonMinus);

            productManager = ProductManager.getInstance(itemView.getContext());
        }

        public void setTxtViewTotalPrice(TextView textView) {
            this.txtViewTotalPrice = textView;
        }

        public void binding(Product product) {
            setProductHolderInf(product);

            setSumPrice(product);

            setTotalPrice();

            imgButtonPlus.setOnClickListener(view -> {
                productManager.addQuantity(product);

                updateItems();
                setTotalPrice();
                setSumPrice(product);
            });

            imgButtonMinus.setOnClickListener(view -> {
                productManager.minusQuantity(product);

                updateItems();
                setTotalPrice();
                setSumPrice(product);
            });

            imgViewThumbnail.setOnClickListener(view -> {});

        }

        private void setProductHolderInf(Product product){
            imgViewThumbnail.setImageBitmap(BitMapService.getBitmapFromURL(product.getThumbnail()));
            txtViewProductName_cart.setText(product.getName());
            txtViewUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            txtViewQuantity.setText(String.valueOf(product.getQuantity()));
        }

        private void setSumPrice(Product product){
            txtViewSumPrice_cart.setText(String.valueOf(product.getUnitPrice() * product.getQuantity()));
        }

        private void setTotalPrice(){
            int totalPrice = productManager.all().stream().mapToInt(p -> (p.getQuantity() * p.getUnitPrice())).sum();
            txtViewTotalPrice.setText(String.valueOf(totalPrice));
        }

        private void updateItems() {
            Log.d("BEFORE", String.valueOf(productList));
            productList.clear();
            productManager.all().forEach(x -> {
                if (x.getQuantity() > 0) {
                    productList.add(x);
                }
            });
            Log.d("AFTER", String.valueOf(productList));
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);

        TextView txtViewTotalPrice = parent.getRootView().findViewById(R.id.txtViewTotalPrice);
        MyCardHolder myCardHolder = new MyCardHolder(itemView);
        myCardHolder.setTxtViewTotalPrice(txtViewTotalPrice);

        return myCardHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCardHolder holder, int position) {
        Product product = productList.get(position);
        holder.binding(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
