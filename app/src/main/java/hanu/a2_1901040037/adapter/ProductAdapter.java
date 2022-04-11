package hanu.a2_1901040037.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040037.models.BitMapService;
import hanu.a2_1901040037.R;
import hanu.a2_1901040037.db.ProductManager;
import hanu.a2_1901040037.models.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> productList;


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    private ProductManager productManager;

    protected class ProductHolder extends RecyclerView.ViewHolder {
        private final ImageView imgViewProduct;

        private final TextView txtViewProductDescription;
        private final TextView txtViewUnitPrice;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            imgViewProduct = itemView.findViewById(R.id.imgViewProduct);
            txtViewProductDescription = itemView.findViewById(R.id.txtViewProductDescription);
            txtViewUnitPrice = itemView.findViewById(R.id.txtViewUnitPrice);
            ImageButton imgBtnAddToCart = itemView.findViewById(R.id.imgBtnAddToCart);

            imgBtnAddToCart.setOnClickListener(view -> {
                int position = getAdapterPosition();
                int id = position + 1;
                productManager = ProductManager.getInstance(view.getContext());
                Product product = productList.get(position);

                if (!productManager.isExistById(id)) {
                    productManager.add(product);
                    return;
                }
                productManager.addQuantity(product);
//                Toast.makeText(view.getContext(), "Selected " + product.getName().substring(0, product.getName().length() / 3), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_main, parent, false);

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productList.get(position);

        holder.imgViewProduct.setImageBitmap(BitMapService.getBitmapFromURL(product.getThumbnail()));
        holder.txtViewProductDescription.setText(product.getName());
        holder.txtViewUnitPrice.setText(String.valueOf(product.getUnitPrice()));
    }

    // method for filtering our recyclerview items.
    public void filterList(List<Product> filteredList) {
        // below line is to add our filtered
        // list in our course array list.
        productList = filteredList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
