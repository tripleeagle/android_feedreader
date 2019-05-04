package cz.cvut.fit.biand.feedreader.repository.dao;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;

@Dao
public interface EntryDao {
    @Query("SELECT * from entry")
    LiveData<List<Entry>> getAllEntries();

    @Query("SELECT id FROM entry WHERE link = :link")
    Long findEntryByLink(String link);

    @Query("SELECT * FROM entry WHERE id = :id")
    LiveData<Entry> findEntryById(long id);

    @Insert
    long insertEntry(Entry entry);

    @Update
    int updateEntry(Entry entry);

    @Query("DELETE FROM entry WHERE " +
            "feed_id IN (SELECT id FROM feed WHERE link = :feedLink) " +
            "AND update_date < :olderThan")
    int deleteEntriesOlderThan(String feedLink, Date olderThan);
}
