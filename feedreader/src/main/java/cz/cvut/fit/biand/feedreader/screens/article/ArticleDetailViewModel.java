package cz.cvut.fit.biand.feedreader.screens.article;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import cz.cvut.fit.biand.feedreader.FeedReaderApplication;
import cz.cvut.fit.biand.feedreader.repository.Repository;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;

public class ArticleDetailViewModel extends AndroidViewModel {

    private final Repository repository;
    private LiveData<Entry> entry;

    public ArticleDetailViewModel(@NonNull Application application) {
        super(application);
        repository = FeedReaderApplication.get(application).getRepository();
    }

    void setEntryId(long entryId) {
        /*if (entry != null) {
            throw new IllegalStateException("entryId can be set only once!");
        }*/
        entry = repository.getEntry(entryId);
    }

    public LiveData<Entry> getArticle() {
        if (entry == null) {
            throw new IllegalStateException("setEntryId was not called!");
        }
        return entry;
    }
}
