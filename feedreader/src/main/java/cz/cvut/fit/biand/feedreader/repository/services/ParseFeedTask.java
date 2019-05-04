package cz.cvut.fit.biand.feedreader.repository.services;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.lifecycle.MutableLiveData;
import cz.cvut.fit.biand.feedreader.repository.Repository;

public class ParseFeedTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "ParseFeedTask";
    private final Repository repository;
    private final MutableLiveData<DownloadStatus> downloadStatus;

    public ParseFeedTask(Repository repository,
                         MutableLiveData<DownloadStatus> downloadStatus) {
        this.repository = repository;
        this.downloadStatus = downloadStatus;
    }

    @Override
    protected void onPreExecute() {
        downloadStatus.setValue(DownloadStatus.RUNNING);
    }

    @Override
    // It's safe, because SyndFeed.getEntries() always returns List of SyndEntry objects.
    @SuppressWarnings("unchecked")
    protected Boolean doInBackground(String... urls) {
        SystemClock.sleep(2000);
        boolean exceptionThrown = false;
        for (String url : urls) {
            // Check if the task didn't get cancelled.
            if (isCancelled()) {
                return false;
            }

            SyndFeedInput input = new SyndFeedInput();
            try {
                URL feedUrl = new URL(url);
                SyndFeed feed = input.build(new InputStreamReader(feedUrl.openStream()));

                repository.saveSyndFeed(url, feed);
                repository.deleteOldEntries(url);
            } catch (MalformedURLException e) {
                Log.w(TAG, "Bad Atom feed url: " + url, e);
                exceptionThrown = true;
            } catch (IOException e) {
                Log.w(TAG, "Can't open Atom feed: " + url, e);
                exceptionThrown = true;
            } catch (FeedException e) {
                Log.w(TAG, "Can't read Atom feed: " + url, e);
                exceptionThrown = true;
            }
        }
        return !exceptionThrown;
    }

    @Override
    // It's safe, because SyndFeed.getEntries() always returns List of SyndEntry objects.
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Boolean success) {
        Log.d ("TAG","onpost execute");
        downloadStatus.setValue(success ? DownloadStatus.FINISHED : DownloadStatus.FAILED);
    }
}
