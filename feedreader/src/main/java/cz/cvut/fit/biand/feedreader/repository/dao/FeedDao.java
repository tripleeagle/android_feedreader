package cz.cvut.fit.biand.feedreader.repository.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

@Dao
public interface FeedDao {
    @Query("SELECT * FROM feed")
    LiveData<List<Feed>> getAllFeeds();

    @Query("SELECT id FROM feed WHERE download_url = :downloadUrl")
    Long findFeedByDownloadUrl(String downloadUrl);

    @Insert
    long insertFeed(Feed feed);

    @Update
    void updateFeed(Feed feed);

    @Query("DELETE FROM feed WHERE id = :id")
    int deleteFeed(long id);
}
