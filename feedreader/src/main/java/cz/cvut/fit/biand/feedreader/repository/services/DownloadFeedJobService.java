package cz.cvut.fit.biand.feedreader.repository.services;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class DownloadFeedJobService extends JobService {

    private DownloadTask downloadTask;

    public DownloadFeedJobService() {
        downloadTask = new DownloadTask(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        downloadTask.downloadFeeds();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        downloadTask.cancelDownload();
        return false;
    }


}
