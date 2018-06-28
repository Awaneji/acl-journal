package com.migs.learn.acl.journalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.database.models.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.JournalEntryViewHolder> {
    private final LayoutInflater inflater;
    private List<JournalEntry> entryList;
    private JournalClickHandler clickHandler;


    public JournalEntryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setEntryList(List<JournalEntry> entryList) {
        this.entryList = entryList;
        notifyDataSetChanged();
    }

    public void setClickHandler(JournalClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public JournalEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use list item to inflate a view and return ViewHolder with that view
        View view = inflater.inflate(R.layout.journal_list_item, parent, false);
        return new JournalEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalEntryViewHolder holder, int position) {
        // get item in recycler by adapter position and populate the ViewHolder accordingly
        JournalEntry journalEntry = entryList.get(position);
        holder.itemView.setTag(journalEntry);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date(journalEntry.getCreateDate()));

        holder.tvTitle.setText(journalEntry.getTitle());
        holder.tvContent.setText(journalEntry.getContent());
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return entryList != null ? entryList.size() : 0;
    }

    public interface JournalClickHandler {
        void onClickJournal(Integer journalId);
    }

    class JournalEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // declared final views here, as drawn by your list item
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvDate;

        public JournalEntryViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            int journalId = entryList.get(position).getJournalId();
            clickHandler.onClickJournal(journalId);

            notifyDataSetChanged();
        }
    }
}
