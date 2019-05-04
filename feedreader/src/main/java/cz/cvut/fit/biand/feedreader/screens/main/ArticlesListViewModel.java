package cz.cvut.fit.biand.feedreader.screens.main;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;

/**
 * View model for list of articles.
 */
public class ArticlesListViewModel extends AndroidViewModel {

    private LiveData<List<Entry>> entries;

    public ArticlesListViewModel(@NonNull Application application) {
        super(application);

        Repository repository = FeedReaderApplication.get(application).getRepository();
        // It's OK to just create the LiveData instance here, it won't do anything until someone
        // calls 'observe' on it.
        entries = repository.getAllEntries();
    }

    /**
     * Gets all articles from all feeds.
     *
     * @return
     */
    LiveData<List<Entry>> getArticles() {
        return entries;
    }
}
