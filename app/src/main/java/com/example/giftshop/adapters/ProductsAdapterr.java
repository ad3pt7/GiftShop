package com.example.giftshop.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftshop.activities.listeners.LikeListener;
import com.example.giftshop.databinding.CatalogItemBinding;
import com.example.giftshop.models.Product;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductsAdapterr  extends  RecyclerView.Adapter<ProductsAdapterr.ProductViewHolder> {

    private final List<Product> products;
    private final LikeListener likeListener;

    public ProductsAdapterr(List<Product> products, LikeListener likeListener) {
        this.products = products;
        this.likeListener = likeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CatalogItemBinding catalogItemBinding = CatalogItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ProductViewHolder(catalogItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
    holder.setProductData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        CatalogItemBinding binding;

        public ProductViewHolder(CatalogItemBinding catalogItemBinding) {
            super(catalogItemBinding.getRoot());
            binding = catalogItemBinding;
        }

        void setProductData(Product product) {
            binding.textProductName.setText(product.name);
            binding.textProductDescr.setText(product.description);
            binding.textCountLikes.setText(String.valueOf(product.likes));
            binding.getRoot().setOnClickListener(v -> likeListener.onLikeClicked(product));
        }
    }


}
