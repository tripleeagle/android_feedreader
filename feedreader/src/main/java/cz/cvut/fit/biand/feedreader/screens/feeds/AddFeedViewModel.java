package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

/**
 * View model for Add feed dialog.
 */
public class AddFeedViewModel extends AndroidViewModel {
    private final Repository repository;

    public AddFeedViewModel(@NonNull Application application) {
        super(application);
        repository = FeedReaderApplication.get(application).getRepository();
    }

    /**
     * Adds a new feed with {@code feedUrl}.
     *
     * @param feedUrl The url of the feed.
     */
    void addFeed(String feedUrl) {
        repository.saveFeed(new Feed(feedUrl));
        // We don't download the new feed automatically so we can test that the Refresh button
        // actually works. But in a real app, it'd be better to immediately try it, or maybe even
        // indicate if the feedUrl is valid or not.
    }
}
