package cz.cvut.fit.biand.feedreader.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.screens.feeds.FeedConfigurationActivity;

/**
 * Main Activity of the application. It contains the {@link ArticlesListFragment} which shows the list
 * of articles from the feed.
 *
 * @author Ondrej Cermak
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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