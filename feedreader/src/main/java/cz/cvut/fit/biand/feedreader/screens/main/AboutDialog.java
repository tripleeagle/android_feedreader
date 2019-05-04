package cz.cvut.fit.biand.feedreader.screens.main;

import java.util.Calendar;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import cz.cvut.fit.biand.feedreader.R;

/**
 * Dialog showing the 'about' text.
 *
 * @author Ondrej Cermak
 */
public class AboutDialog extends DialogFragment {
    /**
     * The tag of this fragment for use with the {@link FragmentManager}.
     */
    private static final String FRAGMENT_TAG = "about_dialog";

    private static final String LOG_TAG = "AboutDialog";

    /**
     * Shows the dialog.
     *
     * @param manager The fragment manager.
     */
    public static void show(FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(FRAGMENT_TAG);
        // If the fragment is already added, don't create a new one
        if (fragment == null) {
            AboutDialog dialog = new AboutDialog();
            dialog.show(manager, FRAGMENT_TAG);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView copyright = view.findViewById(R.id.about_copyright);
        copyright.setText(
                getString(R.string.about_copyright, Calendar.getInstance().get(Calendar.YEAR)));

        TextView version = view.findViewById(R.id.about_version);
        try {
            Context context = view.getContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version.setText(getString(R.string.about_version, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(LOG_TAG, "Can't find our app, shouldn't happen.", e);
            version.setText(
                    getString(R.string.about_version, getString(R.string.about_version_unknown)));
        }

        Button close = view.findViewById(R.id.about_close);
        close.setOnClickListener(v -> dismiss());
    }
}
