package cz.cvut.fit.biand.feedreader.screens.feeds;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import cz.cvut.fit.biand.feedreader.R;

/**
 * Dialog for adding a Feed.
 *
 * @author lyalival
 */
public class AddFeedDialog extends DialogFragment {

    private AddFeedViewModel viewModel;

    private EditText feedUrl;

    public static void show(FragmentManager fragmentManager) {
        String tag = AddFeedDialog.class.getCanonicalName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Look for existing instance of the fragment first, just to be sure.
        Fragment existingDialog = fragmentManager.findFragmentByTag(tag);
        if (existingDialog != null) {
            // If found, remove it.
            transaction.remove(existingDialog);
        }

        // Create and show new dialog.
        AddFeedDialog newDialog = new AddFeedDialog();
        newDialog.show(transaction, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AddFeedViewModel.class);
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_feed, null);
        feedUrl = view.findViewById(R.id.add_feed_url);
        feedUrl.setText("https://fit.cvut.cz/rss-novinky.xml");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_feed_dialog_title);
        builder.setView(view);

        builder.setPositiveButton(R.string.add_feed_ok, (dialog, which) -> {
            String feedUrl = AddFeedDialog.this.feedUrl.getText().toString();
            if (!feedUrl.isEmpty()) {
                // Just insert the link to the database, the data will show up on the next refresh.
                viewModel.addFeed(feedUrl);
                Toast.makeText(getActivity(), R.string.add_feed_added, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.add_feed_url_empty, Toast.LENGTH_SHORT).show();
            }
        });

        // No listener is needed, the dialog closes on any button click
        builder.setNegativeButton(R.string.add_feed_cancel, null);

        builder.setCancelable(true);

        return builder.create();
    }
}
