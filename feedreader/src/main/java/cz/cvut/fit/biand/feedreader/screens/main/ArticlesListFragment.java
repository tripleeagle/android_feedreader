package cz.cvut.fit.biand.feedreader.screens.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;
import cz.cvut.fit.biand.feedreader.repository.services.DownloadStatus;
import cz.cvut.fit.biand.feedreader.screens.article.ArticleDetailActivity;
import cz.cvut.fit.biand.feedreader.screens.article.ArticleDetailFragment;

/**
 * Shows the list of articles from the feed.
 *
 * @author lyalival
 */
public class ArticlesListFragment extends Fragment implements FeedEntriesAdapter.EntryClickedListener {
    private ArticlesListViewModel viewModel;
    private DownloadFeedsViewModel downloadFeedsViewModel;

    private FeedEntriesAdapter adapter;
    private MenuItem refreshItem;

    private RecyclerView recyclerView;
    private TextView emptyText;
    private View progressActionView;

    private boolean wideScreen;

    public static final String EXTRA_LARGE_SCREEN = "largeScreen";

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ArticlesListViewModel.class);
        downloadFeedsViewModel = ViewModelProviders.of(this).get(DownloadFeedsViewModel.class);
        progressActionView = LayoutInflater.from(getActivity()).inflate(R.layout.action_view_progress, null);
        wideScreen = getArguments().getBoolean(EXTRA_LARGE_SCREEN);

        // Retains the ParseFeedTask and the FeedEntriesAdapter with loaded data.
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void refreshFeed(boolean initialRefresh) {
        downloadFeedsViewModel.downloadFeeds(this, initialRefresh);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.feed_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FeedEntriesAdapter(LayoutInflater.from(getActivity()), this);
        recyclerView.setAdapter(adapter);

        emptyText = view.findViewById(R.id.feed_list_empty_text);
        setTextForEmptyRecycler(getString(R.string.list_empty));
        showRecyclerOrEmptyText();

        viewModel.getArticles().observe(this, entriesList -> {
            adapter.setData(entriesList);
            showRecyclerOrEmptyText();
        });

        downloadFeedsViewModel.getDownloadStatus().observe(this, status -> {
            switch (status) {
                case NOT_STARTED:
                    // Refresh the entries when the app starts.
                    refreshFeed(/* initial refresh */ true);
                    break;
                case RUNNING:
                    if (refreshItem != null) {
                        showProgressInRefreshItem();
                    }
                    setTextForEmptyRecycler(getString(R.string.list_loading));
                    break;
                case FAILED:
                    Toast.makeText(getActivity(), getString(R.string.list_loading_failed),
                                   Toast.LENGTH_SHORT)
                         .show();
                    if (refreshItem != null) {
                        refreshItem.setActionView(null);
                    }
                    setTextForEmptyRecycler(getString(R.string.list_empty));
                    break;
                case FINISHED:
                    if (refreshItem != null) {
                        refreshItem.setActionView(null);
                    }
                    setTextForEmptyRecycler(getString(R.string.list_empty));
                    break;
            }
        });
    }

    private void setTextForEmptyRecycler(@Nullable String text) {
        emptyText.setText(text);
    }

    private void showRecyclerOrEmptyText() {
        emptyText.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEntryClicked(Entry entry) {
        if ( wideScreen ){
            Fragment fragment2 = new ArticleDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(ArticleDetailActivity.EXTRA_ID, entry.getId());
            fragment2.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.fragmentRightPlaceHolder, fragment2).commit();
        } else {
            ArticleDetailActivity.startActivity(getActivity(), entry.getId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed_list, menu);
        refreshItem = menu.findItem(R.id.menu_refresh);
        // We can directly call 'getValue' here because if the value is already there, we'll show
        // the progress. If there's no value, the progress will be shown when the value comes and
        // we get it in the observer in onViewCreated.
        if (downloadFeedsViewModel.getDownloadStatus().getValue()
                == DownloadStatus.RUNNING) {
            showProgressInRefreshItem();
        }
    }

    private void showProgressInRefreshItem() {
        refreshItem.setActionView(progressActionView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshFeed(/* initial refresh */ false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
