package cz.cvut.fit.biand.feedreader.repository.entities;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "feed",
        indices = {@Index(value = {"download_url"}, unique = true)}
)
public class Feed {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final Long id;

    /**
     * Uri/id of the feed.
     */
    @ColumnInfo(name = "uri")
    private final String uri;

    /**
     * The original URL used to download the feed.
     */
    @ColumnInfo(name = "download_url")
    private final String downloadUrl;

    /**
     * Link to the feed.
     */
    @ColumnInfo(name = "link")
    private final String link;

    /**
     * Title of the feed.
     */
    @ColumnInfo(name = "title")
    private final String title;

    /**
     * Subtitle/description of the feed.
     */
    @ColumnInfo(name = "subtitle")
    private final String subtitle;

    /**
     * The date the feed was last updated (in millis since epoch).
     */
    @ColumnInfo(name = "update_date")
    private final Date updatedDate;

    /**
     * The default author of the feed.
     */
    @ColumnInfo(name = "author")
    private final String author;

    public Feed(@Nullable Long id, String uri, String downloadUrl, String link, String title, String subtitle,
                Date updatedDate, String author) {
        this.id = id;
        this.uri = uri;
        this.downloadUrl = downloadUrl;
        this.link = link;
        this.title = title;
        this.subtitle = subtitle;
        this.updatedDate = updatedDate;
        this.author = author;
    }

    @Ignore
    public Feed(String downloadUrl) {
        this(null, "", downloadUrl, "", "", "", new Date(0), "");
    }

    public Long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
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
        Feed feed = (Feed) o;
        return Objects.equals(id, feed.id) &&
                Objects.equals(uri, feed.uri) &&
                Objects.equals(downloadUrl, feed.downloadUrl) &&
                Objects.equals(link, feed.link) &&
                Objects.equals(title, feed.title) &&
                Objects.equals(subtitle, feed.subtitle) &&
                Objects.equals(updatedDate, feed.updatedDate) &&
                Objects.equals(author, feed.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri, downloadUrl, link, title, subtitle, updatedDate, author);
    }
}
