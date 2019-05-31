package cz.cvut.fit.biand.feedreader.repository.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.db.FeedReaderDatabase;

public class DownloadTask {

    private Context context;
    private final Repository repository;

    @Nullable
    private ParseFeedTask parseFeedTask;

    public DownloadTask(Context context) {
        this.context = context;
        repository = FeedReaderApplication.getRepository();
        repository.changeStatusValue(DownloadStatus.NOT_STARTED);
    }

    public void downloadFeeds() {
        if (parseFeedTask == null || parseFeedTask.getStatus() == AsyncTask.Status.FINISHED)
        {
            parseFeedTask = new ParseFeedTask(repository, repository.getDownloadStatus());
            LiveData<List<String>> urlsLiveData = repository.getAllFeedDownloadUrls();
            urlsLiveData .observeForever( new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> urls) {
                    if ( !urls.isEmpty()) {
                        // Wait till there's at least one Feed to download.
                        urlsLiveData.removeObserver(this);
                        parseFeedTask.execute(urls.toArray(new String[0]));
                    }
                }
            });

        }
        // Otherwise there's already a task which is either PENDING (it'll run eventually) or RUNNING.
    }

    public void cancelDownload() {
        if (parseFeedTask != null) {
            parseFeedTask.cancel(true);
            parseFeedTask = null;
        }
    }

}
