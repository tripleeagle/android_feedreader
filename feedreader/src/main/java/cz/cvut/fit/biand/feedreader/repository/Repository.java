package cz.cvut.fit.biand.feedreader.repository;

import android.text.Html;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import cz.cvut.fit.biand.feedreader.repository.dao.EntryDao;
import cz.cvut.fit.biand.feedreader.repository.dao.FeedDao;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;
import cz.cvut.fit.biand.feedreader.repository.services.DownloadStatus;

public class Repository {
    private final FeedDao feedDao;
    private final EntryDao entryDao;

    private final LiveData<List<Feed>> feedsLiveData;
    private final LiveData<List<Entry>> entriesLiveData;

    private final MutableLiveData<DownloadStatus> downloadStatus = new MutableLiveData<>();

    public Repository(FeedDao feedDao, EntryDao entryDao) {
        this.feedDao = feedDao;
        this.entryDao = entryDao;
        feedsLiveData = feedDao.getAllFeeds();
        entriesLiveData = entryDao.getAllEntries();
    }

    public LiveData<List<Feed>> getAllFeeds() {
        return feedsLiveData;
    }

    public LiveData<List<String>> getAllFeedDownloadUrls() {
        return Transformations.map(getAllFeeds(), feeds -> {
            List<String> urls = new ArrayList<>(feeds.size());
            for (Feed feed : feeds) {
                urls.add(feed.getDownloadUrl());
            }
            return urls;
        });
    }

    public LiveData<List<Entry>> getAllEntries() {
        return entriesLiveData;
    }

    public LiveData<Entry> getEntry(long id) {
        return entryDao.findEntryById(id);
    }

    public void deleteFeed(long id) {
        new AsyncRoomOp<>(feedDao::deleteFeed).execute(id);
    }

    public void saveFeed(Feed feed) {
        new AsyncRoomOp<>(feedDao::insertFeed).execute(feed);
    }

    @WorkerThread
    @SuppressWarnings("unchecked") // SyndFeed is unfortunately not generic enough.
    public void saveSyndFeed(String url, SyndFeed syndFeed) {
        Long feedId = feedDao.findFeedByDownloadUrl(url);
        if (feedId != null) {
            feedDao.updateFeed(createFeedFromSyndFeed(feedId, url, syndFeed));
        } else {
            feedId = feedDao.insertFeed(createFeedFromSyndFeed(null, url, syndFeed));
        }
        saveSyndFeedEntries(feedId, (List<SyndEntry>) syndFeed.getEntries());
    }

    private Feed createFeedFromSyndFeed(@Nullable Long id, String downloadUrl, SyndFeed syndFeed) {
        return new Feed(id, syndFeed.getUri(), downloadUrl, syndFeed.getLink(), syndFeed.getTitle(),
                syndFeed.getDescription(), new Date(), syndFeed.getAuthor());
    }

    private void saveSyndFeedEntries(long feedId, List<SyndEntry> entries) {
        for (SyndEntry entry : entries) {
            Long entryId = entryDao.findEntryByLink(entry.getLink());
            if (entryId != null) {
                entryDao.updateEntry(createEntryFromSyndEntry(entryId, feedId, entry));
            } else {
                entryDao.insertEntry(createEntryFromSyndEntry(null, feedId, entry));
            }
        }
    }

    private Entry createEntryFromSyndEntry(@Nullable Long id, long feedId, SyndEntry syndEntry) {
        String desc;
        if (syndEntry.getDescription() != null) {
            desc = syndEntry.getDescription().getValue();
        } else if (syndEntry.getContents().size() > 0) {
            desc = ((SyndContent) syndEntry.getContents().get(0)).getValue();
        } else {
            desc = "";
        }

        String content;
        if (syndEntry.getContents().size() > 0) {
            content = ((SyndContent) syndEntry.getContents().get(0)).getValue();
        } else if (syndEntry.getDescription() != null) {
            content = syndEntry.getDescription().getValue();
        } else {
            content = "";
        }

        Date date = syndEntry.getUpdatedDate();
        if (date == null) {
            date = syndEntry.getPublishedDate();
            if (date == null) {
                date = new Date(0);
            }
        }

        return new Entry(id, feedId, syndEntry.getUri(), syndEntry.getLink(),
                syndEntry.getTitle(), prepareSummary(desc), content, date,
                syndEntry.getAuthor());
    }

    private String prepareSummary(String text) {
        text = text.replaceAll("(?s)<style[^>]*>.*?</style>", "");
        text = text.replaceAll("(?s)<img.*?/>", "");
        text = Html.fromHtml(text).toString();
        if (text.length() > 300) {
            text = text.substring(0, 300);
        }
        return text.trim();
    }

    @WorkerThread
    public void deleteOldEntries(String feedLink) {
        Calendar date = Calendar.getInstance();
        date.roll(Calendar.MONTH, /* up */ false);

        entryDao.deleteEntriesOlderThan(feedLink, date.getTime());
    }

    public MutableLiveData<DownloadStatus> getDownloadStatus() {
        return downloadStatus;
    }

    public void changeStatusValue(DownloadStatus downloadStatus) {
        this.downloadStatus.setValue(downloadStatus);
    }
}
