package cz.cvut.fit.biand.feedreader.screens.main;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.services.DownloadStatus;
import cz.cvut.fit.biand.feedreader.repository.services.ParseFeedTask;

/**
 * View model for downloading feeds.
 */
public class DownloadFeedsViewModel extends AndroidViewModel {
    private final Repository repository;
    @Nullable
    private ParseFeedTask downloadTask;

    public DownloadFeedsViewModel(@NonNull Application application) {
        super(application);
        repository = FeedReaderApplication.get(application).getRepository();
        repository.changeStatusValue(DownloadStatus.NOT_STARTED);
    }

    public void downloadFeeds(LifecycleOwner lifecycleOwner, boolean atLeastOneFeedRequired) {
        if (downloadTask == null || downloadTask.getStatus() == AsyncTask.Status.FINISHED) {
            downloadTask = new ParseFeedTask(repository, repository.getDownloadStatus());

            LiveData<List<String>> urlsLiveData = repository.getAllFeedDownloadUrls();
            urlsLiveData.observe(lifecycleOwner, new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> urls) {
                    if (!atLeastOneFeedRequired || !urls.isEmpty()) {
                        // Wait till there's at least one Feed to download.
                        urlsLiveData.removeObserver(this);
                        downloadTask.execute(urls.toArray(new String[0]));
                    }
                }
            });
        }
        // Otherwise there's already a task which is either PENDING (it'll run eventually) or RUNNING.
    }

    @Override
    protected void onCleared() {
        cancelDownload();
    }

    /**
     * Cancels a running download if there's one.
     */
    private void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel(true);
            downloadTask = null;
        }
    }


    // Notice we return LiveData and not MutableLiveData. It's always good to limit access as much
    // as possible to avoid stupid bugs.
    LiveData<DownloadStatus> getDownloadStatus() {
        return repository.getDownloadStatus();
    }

}
