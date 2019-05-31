package cz.cvut.fit.biand.feedreader.repository.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import cz.cvut.fit.biand.feedreader.repository.dao.EntryDao;
import cz.cvut.fit.biand.feedreader.repository.dao.FeedDao;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;
import cz.cvut.fit.biand.feedreader.repository.entities.Feed;

@Database(entities = {Feed.class, Entry.class}, version = FeedReaderDatabase.DB_VERSION)
@TypeConverters({DateConverter.class})
public abstract class FeedReaderDatabase extends RoomDatabase {

    static final int DB_VERSION = 1;
    private static final String DB_NAME = "feedreader.db";
    @Nullable
    private static FeedReaderDatabase instance;

    public static synchronized FeedReaderDatabase getInstance(Context context) {
        if (instance == null) {
            Context appContext = context.getApplicationContext();
            instance = Room
                    .databaseBuilder(appContext,
                            FeedReaderDatabase.class, DB_NAME)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Defer this to a background thread so we don't call getInstance
                            // recursively.
                            // TODO: This being asynchronous causes the first start of the app to not see any feed urls.
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    generateInitialData(getInstance(appContext));
                                    return null;
                                }
                            }.execute();
                        }
                    })
                    .build();
        }
        return instance;
    }

    private static void generateInitialData(FeedReaderDatabase db) {
        FeedDao dao = db.feedDao();

        Feed technetFeed = new Feed(null, "", "http://servis.idnes.cz/rss.aspx?c=technet", "", "", "", new Date(0), "");
        Feed androidDevelopersFeed =
                new Feed(null, "", "http://android-developers.blogspot.com/atom.xml", "", "", "", new Date(0), "");
        dao.insertFeed(technetFeed);
        dao.insertFeed(androidDevelopersFeed);
    }

    public abstract FeedDao feedDao();

    public abstract EntryDao entryDao();
}
