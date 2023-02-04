package com.example.giftshop.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftshop.R;
import com.example.giftshop.models.Product;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {


    private ArrayList<Product> products = new ArrayList<>();
    private onLikeClickListener onClickListener;
    public interface onLikeClickListener{
        void onLikeClick(Product Product);
    }

    public void setOnClickListener(onLikeClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyDataSetChanged();
    }

    public void addProduct(Product product){
        this.products.add(product);
        notifyDataSetChanged();
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.catalog_item,
                parent,
                false
        );
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position)
    {
        Product product = products.get(position);
        final Boolean[] isLiked = {false};
        holder.name.setText(product.name);
        holder.description.setText(product.description);
        holder.productPhoto.setImageDrawable(ContextCompat.getDrawable(
                holder.productPhoto.getContext(),R.drawable.iphone14));
        holder.likes.setText(String.valueOf(product.likes));
        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLiked[0]){
                    holder.liked.setColorFilter(R.color.red);
                    Log.d("firebase","liked");
                }else{
                    holder.liked.clearColorFilter();
                    Log.d("firebase","disliked");
                }
                isLiked[0] = !isLiked[0];
                Log.d("firebase", String.valueOf(isLiked[0]));
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private ImageView liked;
        private ImageView productPhoto;
        private TextView likes;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.productNameText);
            description = itemView.findViewById(R.id.textProductDescr);
            likes = itemView.findViewById(R.id.textCountLikes);
            productPhoto = itemView.findViewById(R.id.imageView);
            liked = itemView.findViewById(R.id.imageLike);

        }
    }
}


