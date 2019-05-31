package cz.cvut.fit.biand.feedreader;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.db.FeedReaderDatabase;
import cz.cvut.fit.biand.feedreader.repository.services.DownloadFeedJobService;

public class FeedReaderApplication extends Application {
    private static Repository repository;

    public static FeedReaderApplication get(Context context) {
        return (FeedReaderApplication) context.getApplicationContext();
    }

    public static Repository getRepository() {
        return repository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRoom();
        addDownloadSchedule();
    }

    private void addDownloadSchedule() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, DownloadFeedJobService.class);

        jobScheduler.schedule(new JobInfo.Builder(1, componentName)
                .setPeriodic(1000 * 60 * 15)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build());
    }

    private void initRoom() {
        FeedReaderDatabase db = FeedReaderDatabase.getInstance(this);
        repository = new Repository(db.feedDao(), db.entryDao());
    }
}
