package cz.cvut.fit.biand.feedreader.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.screens.article.ArticleDetailFragment;
import cz.cvut.fit.biand.feedreader.screens.feeds.FeedConfigurationActivity;

/**
 * Main Activity of the application. It contains the {@link ArticlesListFragment} which shows the list
 * of articles from the feed.
 *
 * @author lyalival
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            addFragments();
        }
    }

    /**
     * Adds ArticlesListFragment and ArticleDetailFragment (the last one in case the device is a tablet)
     */
    private void addFragments() {
        FragmentManager manager = getSupportFragmentManager();
        if (findViewById(R.id.container) != null && manager.findFragmentById(R.id.container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_list_wrapper, createArticleListFragment(false));
            transaction.commit();
        }
        if (findViewById(R.id.large_container) != null && manager.findFragmentById(R.id.large_container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_list_wrapper, createArticleListFragment(true));
            transaction.replace(R.id.fragmentRightPlaceHolder, createArticleDetailFragment());
            transaction.commit();
        }
    }

    private Fragment createArticleListFragment(boolean isWideScreen) {
        getIntent().putExtra(ArticlesListFragment.EXTRA_LARGE_SCREEN, isWideScreen);
        return Fragment.instantiate(this, ArticlesListFragment.class.getName(),
                getIntent().getExtras());
    }

    private Fragment createArticleDetailFragment() {
        return Fragment.instantiate(this, ArticleDetailFragment.class.getName(),
                getIntent().getExtras());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutDialog.show(getSupportFragmentManager());
                return true;
            case R.id.menu_configure_feeds:
                Intent intent = new Intent(this, FeedConfigurationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}