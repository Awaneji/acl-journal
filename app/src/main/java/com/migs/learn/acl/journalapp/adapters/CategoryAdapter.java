package com.migs.learn.acl.journalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.database.models.JournalCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final LayoutInflater inflater;
    private List<JournalCategory> categoryList;
    private CategoryClickHandler categoryClickHandler;

    public CategoryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public List<JournalCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<JournalCategory> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public void setCategoryClickHandler(CategoryClickHandler categoryClickHandler) {
        this.categoryClickHandler = categoryClickHandler;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        JournalCategory category = categoryList.get(position);
        holder.itemView.setTag(category);
        holder.tvCategory.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public interface CategoryClickHandler {
        void onClickCategory(Integer categoryId);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvCategory;

        private CategoryViewHolder(View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            JournalCategory category = categoryList.get(adapterPosition);

            // do click stuff
            categoryClickHandler.onClickCategory(category.getCategoryId());
        }
    }
}
