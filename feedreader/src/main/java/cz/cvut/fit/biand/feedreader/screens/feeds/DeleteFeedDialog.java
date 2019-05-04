package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import cz.cvut.fit.biand.feedreader.R;

/**
 * Dialog for deleting a Feed.
 *
 * @author Ondrej Cermak
 */
public class DeleteFeedDialog extends DialogFragment {
    private static final String ARG_FEED_ID = "feed_id";

    private long feedId;

    private DeleteFeedViewModel viewModel;

    /**
     * Shows the {@link cz.cvut.fit.biand.feedreader.screens.feeds.DeleteFeedDialog}.
     *
     * @param fragmentManager The fragment manager.
     */
    public static void show(FragmentManager fragmentManager, long feedId) {
        String tag = DeleteFeedDialog.class.getCanonicalName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Look for existing instance of the fragment first, just to be sure.
        Fragment existingDialog = fragmentManager.findFragmentByTag(tag);
        if (existingDialog != null) {
            // If found, remove it.
            transaction.remove(existingDialog);
        }

        // Create and show new dialog.
        DeleteFeedDialog newDialog = new DeleteFeedDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_FEED_ID, feedId);
        newDialog.setArguments(args);
        newDialog.show(transaction, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedId = getArguments().getLong(ARG_FEED_ID);
        viewModel = ViewModelProviders.of(this).get(DeleteFeedViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_feed_dialog_title);
        builder.setMessage(R.string.delete_feed_text);

        builder.setPositiveButton(R.string.delete_feed_ok, (dialog, which) -> {
            viewModel.deleteFeed(feedId);
            Toast.makeText(getActivity(), R.string.delete_feed_deleted, Toast.LENGTH_SHORT).show();
        });

        // No listener is needed, the dialog closes on any button click
        builder.setNegativeButton(R.string.delete_feed_cancel, null);

        builder.setCancelable(true);

        return builder.create();
    }
}
