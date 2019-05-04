package cz.cvut.fit.biand.feedreader.screens.main;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;

public class FeedEntriesAdapter extends RecyclerView.Adapter<FeedEntriesAdapter.ViewHolder> {
    public interface EntryClickedListener {
        void onEntryClicked(Entry entry);
    }
    private final LayoutInflater inflater;
    private final EntryClickedListener listener;
    private List<Entry> data = new ArrayList<>(0);

    public FeedEntriesAdapter(LayoutInflater inflater, EntryClickedListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    public void setData(List<Entry> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_entry, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final EntryClickedListener listener;
        private final TextView title;
        private final TextView summary;

        ViewHolder(@NonNull View itemView, EntryClickedListener listener) {
            super(itemView);
            this.listener = listener;
            title = itemView.findViewById(R.id.item_entry_title);
            summary = itemView.findViewById(R.id.item_entry_summary);
        }

        void bind(Entry entry) {
            itemView.setOnClickListener(itemView -> listener.onEntryClicked(entry));

            title.setText(Html.fromHtml(entry.getTitle()));

            String summaryText = entry.getSummary();
            if (!TextUtils.isEmpty(summaryText)) {
                summary.setText(summaryText);
                summary.setVisibility(View.VISIBLE);
            } else {
                summary.setVisibility(View.GONE);
            }
        }
    }
}
