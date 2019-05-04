package cz.cvut.fit.biand.feedreader.repository.entities;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "entry",
        foreignKeys = {
                @ForeignKey(
                        entity = Feed.class,
                        parentColumns = "id",
                        childColumns = "feed_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("feed_id")
        }
)
public class Entry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final Long id;

    /**
     * The _ID of the feed.
     */
    @ColumnInfo(name = "feed_id")
    private final Long feedId;

    /**
     * Uri/id of the feed entry.
     */
    @ColumnInfo(name = "uri")
    private final String uri;

    /**
     * The link to the entry.
     */
    @ColumnInfo(name = "link")
    private final String link;

    /**
     * The title of the entry.
     */
    @ColumnInfo(name = "title")
    private final String title;

    /**
     * The summary of the entry.
     */
    @ColumnInfo(name = "summary")
    private final String summary;

    /**
     * The content of the entry.
     */
    @ColumnInfo(name = "content")
    private final String content;

    /**
     * The date the entry was last updated (in millis since epoch).
     */
    @ColumnInfo(name = "update_date")
    private final Date updatedDate;

    /**
     * The author of the entry.
     */
    @ColumnInfo(name = "author")
    private final String author;

    public Entry(@Nullable Long id, Long feedId, String uri, String link, String title,
                 String summary, String content, Date updatedDate, String author) {
        this.id = id;
        this.feedId = feedId;
        this.uri = uri;
        this.link = link;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.updatedDate = updatedDate;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public Long getFeedId() {
        return feedId;
    }

    public String getUri() {
        return uri;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id) &&
                Objects.equals(feedId, entry.feedId) &&
                Objects.equals(uri, entry.uri) &&
                Objects.equals(link, entry.link) &&
                Objects.equals(title, entry.title) &&
                Objects.equals(summary, entry.summary) &&
                Objects.equals(content, entry.content) &&
                Objects.equals(updatedDate, entry.updatedDate) &&
                Objects.equals(author, entry.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, feedId, uri, link, title, summary, content, updatedDate, author);
    }
}