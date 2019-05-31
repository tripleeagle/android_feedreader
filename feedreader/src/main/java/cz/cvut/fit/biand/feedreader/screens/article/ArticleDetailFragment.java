package cz.cvut.fit.biand.feedreader.screens.article;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import cz.cvut.fit.biand.feedreader.R;
import cz.cvut.fit.biand.feedreader.repository.entities.Entry;

/**
 * Shows the full article.
 *
 * @author lyalival
 */
public class ArticleDetailFragment extends Fragment {

    static final String ARG_ID = "id";
    private ArticleDetailViewModel viewModel;
    private View container;
    private View progress;
    private TextView title;
    private TextView date;
    private TextView text;
    private TextView fullArticle;
    private Long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ArticleDetailViewModel.class);
        id = getArguments().getLong(ARG_ID);
        viewModel.setEntryId(getArguments().getLong(ARG_ID));
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.entry_title);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_ID, id);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    // It's safe, because SyndEntry.getContents() always returns List of SyncContent objects.
    @SuppressWarnings("unchecked")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = view.findViewById(R.id.artile_detail_container);
        progress = view.findViewById(R.id.artile_detail_progress);

        title = view.findViewById(R.id.article_detail_title);
        date = view.findViewById(R.id.article_detail_date_author);
        text = view.findViewById(R.id.article_detail_text);
        fullArticle = view.findViewById(R.id.article_detail_full_article);

        text.setMovementMethod(LinkMovementMethod.getInstance());
        fullArticle.setText(
                Html.fromHtml("<u>" + getString(R.string.entry_view_full).toUpperCase() + "</u>"));

        viewModel.getArticle().observe(this, entry -> {
            if (entry != null) {
                updateViewsWithContent(entry);

                fullArticle.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(entry.getLink()));
                    startActivity(intent);
                });
            } else {
                title.setText(R.string.entry_not_found);
                date.setVisibility(View.INVISIBLE);
                text.setText(R.string.entry_not_found_desc);
                text.setTextColor(getResources().getColor(R.color.text_error));
                fullArticle.setVisibility(View.GONE);
            }

            container.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        });
    }

    private void updateViewsWithContent(Entry entry) {
        title.setText(Html.fromHtml(entry.getTitle()));
        String author = entry.getAuthor();
        if (TextUtils.isEmpty(author)) {
            author = getString(R.string.entry_unknown_author);
        }
        Date date = entry.getUpdatedDate();
        if (date.getTime() > 0) {
            CharSequence relativeDate = DateUtils.getRelativeTimeSpanString(date.getTime());
            this.date.setText(getString(R.string.entry_date_author, relativeDate, author));
        } else {
            this.date.setText(getString(R.string.entry_date_author, "", author).trim());
        }

        String contentString = entry.getContent();
        contentString = contentString.replaceAll("(?s)<style[^>]*>.*?</style>", "");
        contentString = contentString.replaceAll("(?s)<img.*?/>", "");
        contentString = contentString.replaceAll("(?s)<a[^>]*>\\s*</a>", "");
        contentString = contentString.replaceAll("(?s)<ul[^>]*>(.*?)</ul>", "<p>$1</p>");
        contentString = contentString.replaceAll("(?s)<li[^>]*>(.*?)</li>", "<br/>&bull; $1");
        contentString = contentString.replaceAll("(?s)(<br.*?/>\\s*)+", "<br/>");
        contentString = contentString.trim();
        text.setText(Html.fromHtml(contentString));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article_share:
                viewModel.getArticle().observe(this, entry -> {
                    if (entry == null) {
                        Toast.makeText(getActivity(), R.string.entry_not_loaded, Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.share_subject, entry.getTitle()));
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_text, entry.getLink()));
                    Intent chooser =
                            Intent.createChooser(shareIntent, getString(R.string.share_chooser));
                    startActivity(chooser);
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
