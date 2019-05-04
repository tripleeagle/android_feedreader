package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;

public class DeleteFeedViewModel extends AndroidViewModel {
    private final Repository repository;

    public DeleteFeedViewModel(@NonNull Application application) {
        super(application);
        repository = FeedReaderApplication.get(application).getRepository();
    }

    /**
     * Deletes feed with {@code feedId}.
     *
     * @param feedId The feed id.
     */
    void deleteFeed(long feedId) {
        repository.deleteFeed(feedId);
    }
}
