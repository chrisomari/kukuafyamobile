package com.example.kukuafya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<String> categoryList;
    private final Context context;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0; // Default to "All" category

    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    public CategoryAdapter(Context context, List<String> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.tvCategory.setText(category);

        // Highlight selected category
        if (position == selectedPosition) {
            holder.tvCategory.setBackgroundResource(R.drawable.bg_category_selected);
            holder.tvCategory.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.tvCategory.setBackgroundResource(R.drawable.bg_category_unselected);
            holder.tvCategory.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Update UI for previous and new selection
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            listener.onCategoryClick(category, position);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}
