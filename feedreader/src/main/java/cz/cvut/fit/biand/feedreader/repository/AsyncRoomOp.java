package cz.cvut.fit.biand.feedreader.repository;

import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;

class AsyncRoomOp<Entity, Result> extends AsyncTask<Entity, Void, List<Result>> {
    interface AsyncBlock<Entity, Result> {
        Result insert(Entity entity);
    }

    interface Callback<Result> {
        void onComplete(List<Result> results);
    }

    private final AsyncBlock<Entity, Result> block;

    @Nullable
    private final Callback<Result> callback;

    AsyncRoomOp(AsyncBlock<Entity, Result> block, @Nullable Callback<Result> callback) {
        this.block = block;
        this.callback = callback;
    }

    AsyncRoomOp(AsyncBlock<Entity, Result> block) {
        this(block, null);
    }

    @Override
    protected List<Result> doInBackground(Entity... entities) {
        List<Result> results = new LinkedList<>();
        for (Entity entity : entities) {
            Result result = block.insert(entity);
            results.add(result);
        }
        return results;
    }

    @Override
    protected void onPostExecute(List<Result> results) {
        if (callback != null) {
            callback.onComplete(results);
        }
    }
}
