package com.example.giftshop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {


    private ArrayList<Product> products = new ArrayList<>();
    private onLikeClickListener onClickListener;
    interface onLikeClickListener{
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
        holder.liked.setImageDrawable(ContextCompat.getDrawable(
                holder.liked.getContext(),R.drawable.unliked));
        holder.productPhoto.setImageDrawable(ContextCompat.getDrawable(
                holder.productPhoto.getContext(),R.drawable.iphone14));
        holder.likes.setText(String.valueOf(product.likes));
        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked[0]){
                    holder.liked.setImageDrawable(ContextCompat.getDrawable(
                            holder.liked.getContext(),R.drawable.liked));
                }else{
                    holder.liked.setImageDrawable(ContextCompat.getDrawable(
                            holder.liked.getContext(),R.drawable.unliked));
                }
                isLiked[0] = !isLiked[0];
                Log.d("firebase",product.toString());
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
            description = itemView.findViewById(R.id.productDescription);
            likes = itemView.findViewById(R.id.likesCount);
            productPhoto = itemView.findViewById(R.id.imageView);
            liked = itemView.findViewById(R.id.imageView2);

        }
    }
}


