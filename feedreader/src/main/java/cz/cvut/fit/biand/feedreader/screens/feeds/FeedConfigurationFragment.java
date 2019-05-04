package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

/**
 * Shows the list of added Feeds.
 *
 * @author Ondrej Cermak
 */
public class FeedConfigurationFragment extends Fragment implements
        FeedsAdapter.FeedClickedListener {
    private RecyclerView recyclerView;
    private TextView emptyText;

    private FeedsAdapter adapter;
    private FeedConfigurationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FeedConfigurationViewModel.class);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_configuration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.feed_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FeedsAdapter(LayoutInflater.from(getActivity()), this);
        recyclerView.setAdapter(adapter);

        emptyText = view.findViewById(R.id.feed_list_empty_text);
        showEmptyText(getString(R.string.feed_configuration_no_feeds));

        viewModel.getFeeds().observe(this, feedList -> {
            adapter.setData(feedList);
            if (feedList.isEmpty()) {
                showEmptyText(getString(R.string.feed_configuration_no_feeds));
            } else {
                showEmptyText(null);
            }
        });
    }

    /**
     * Shows/hides the "empty text".
     */
    private void showEmptyText(@Nullable String text) {
        emptyText.setText(text);
        emptyText.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed_configuration, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_feed_add:
                AddFeedDialog.show(getFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFeedClicked(Feed feed) {
        DeleteFeedDialog.show(getFragmentManager(), feed.getId());
    }
}
