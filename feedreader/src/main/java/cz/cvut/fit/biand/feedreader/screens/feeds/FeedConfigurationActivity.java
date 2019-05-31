package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cz.cvut.fit.biand.feedreader.R;

/**
 * Contains the {@link FeedConfigurationFragment} which shows the list of added Feeds.
 *
 * @author lyalival
 */
public class FeedConfigurationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepane);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.feed_configuration);

        addFeedConfigurationFragment();
    }

    private void addFeedConfigurationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment feedsFragment = Fragment.instantiate(this,
                                                          FeedConfigurationFragment.class
                                                                  .getName());
            transaction.add(R.id.container, feedsFragment);
            transaction.commit();
        }
    }
}
