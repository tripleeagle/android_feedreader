package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

/**
 * View model for list of feeds.
 */
public class FeedConfigurationViewModel extends AndroidViewModel {

    private final LiveData<List<Feed>> feeds;

    public FeedConfigurationViewModel(@NonNull Application application) {
        super(application);

        feeds = FeedReaderApplication.get(application).getRepository().getAllFeeds();
    }

    /**
     * Gets list of feeds.
     */
    LiveData<List<Feed>> getFeeds() {
        return feeds;
    }
}
