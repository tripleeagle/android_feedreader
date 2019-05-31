package cz.cvut.fit.biand.feedreader.screens.feeds;

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
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final FeedClickedListener listener;
    private List<Feed> data = new ArrayList<>(0);
    public FeedsAdapter(LayoutInflater inflater, FeedClickedListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    public void setData(List<Feed> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_feed, parent, false);
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

    public interface FeedClickedListener {
        void onFeedClicked(Feed feed);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final FeedClickedListener listener;
        private final TextView title;
        private final TextView url;

        ViewHolder(@NonNull View itemView, FeedClickedListener listener) {
            super(itemView);
            this.listener = listener;
            title = itemView.findViewById(R.id.item_feed_title);
            url = itemView.findViewById(R.id.item_feed_url);
        }

        void bind(Feed feed) {
            itemView.setOnClickListener(itemView -> listener.onFeedClicked(feed));

            String feedTitle = feed.getTitle();
            title.setText(TextUtils.isEmpty(feedTitle) ?
                    itemView.getContext().getText(
                            R.string.feed_configuration_unknown_feed_title) :
                    feedTitle);
            url.setText(feed.getDownloadUrl());
        }
    }
}
