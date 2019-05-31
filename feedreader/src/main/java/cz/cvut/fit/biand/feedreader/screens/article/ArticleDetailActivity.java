package cz.cvut.fit.biand.feedreader.screens.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cz.cvut.fit.biand.feedreader.R;

/**
 * Contains the {@link ArticleDetailFragment} which shows the full article.
 *
 * @author lyalival
 */
public class ArticleDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = ArticleDetailFragment.ARG_ID;

    /**
     * Method is using for starting the ArticleDetailActivity
     * @param entryId is id of the entry to be shown
     */
    public static void startActivity(Context context, long entryId) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.EXTRA_ID, entryId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepane);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addArticleDetailFragment();
    }

    private void addArticleDetailFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, createArticleDetailFragment());
            transaction.commit();
        }
    }

    private Fragment createArticleDetailFragment() {
        return Fragment.instantiate(this, ArticleDetailFragment.class.getName(),
                                    getIntent().getExtras());
    }
}
