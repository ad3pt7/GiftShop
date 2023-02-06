package com.example.giftshop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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
import com.example.giftshop.activities.CatalogActivity;
import com.example.giftshop.models.Product;
import com.example.giftshop.models.ProductsLikes;
import com.example.giftshop.utilities.Constants;
import com.example.giftshop.utilities.Database;
import com.example.giftshop.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.prefs.PreferenceChangeEvent;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    Context mContext;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().
            getReference().child("productsLikes");
    private int id;
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
        SharedPreferences preferences =holder.itemView.getContext().
                getSharedPreferences("giftShopPreference",Context.MODE_PRIVATE);
        id = preferences.getInt("userId",1);
        Product product = products.get(position);
        final Boolean[] isLiked = {false};
        holder.name.setText(product.name);
        holder.description.setText(product.description);
        holder.productPhoto.setImageDrawable(ContextCompat.getDrawable(
                holder.productPhoto.getContext(),R.drawable.iphone14));
        holder.likes.setText(String.valueOf(product.likes));

        for(ProductsLikes like : Database.getInstance().getProductsLikes())
        {
            if(like.userId == preferences.getInt("userId",1) && like.productId == product.id)
            {
                isLiked[0] = true;
                product.likeName = like.name;
            }
        }
        if(isLiked[0]){
            holder.liked.setColorFilter(R.color.red);
        }else{
            holder.liked.clearColorFilter();
        }

        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLiked[0]){
                    holder.liked.setColorFilter(R.color.red);
                    onProductLike(product);
                    //Log.d("firebase","liked");
                }else{
                    holder.liked.clearColorFilter();
                    onProductDislike(product);
                    //Log.d("firebase","disliked");
                }
                isLiked[0] = !isLiked[0];
                //Log.d("firebase", String.valueOf(isLiked[0]));
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
            name =itemView.findViewById(R.id.textProductName);
            description = itemView.findViewById(R.id.textProductDescr);
            likes = itemView.findViewById(R.id.textCountLikes);
            productPhoto = itemView.findViewById(R.id.imageView);
            liked = itemView.findViewById(R.id.imageLike);

        }
    }

    public void onProductLike(Product product)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().
                getReference().child("productsLikes");
        String uniqueID = UUID.randomUUID().toString();
        ProductsLikes like = new ProductsLikes(uniqueID,product.id,id);
        ref.child(uniqueID).setValue(like);
        product.likeName = uniqueID;

    }

    public void onProductDislike(Product product)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().
                getReference().child("productsLikes");
        ref.child(product.likeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("firebase",product.likeName);
    }
}


