package de.cyberforum.openit_project_20151;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import de.cyberforum.openit_project_20151.NewsImplementation.FetchMode;
import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemStatus0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIReaction0;


/**
 * A fragment containing a list view.
 */
public class FragmentList extends Fragment implements NewsUIReaction0 {

    protected FetchMode fetchMode;
    protected ActionBarActivity activity;
    protected NewsAction0 newsAction;
    protected View view;
    protected ListView listView;

    protected class NewsItemStringable {
        protected NewsItemRead0 newsItemRead0;
        NewsItemStringable(NewsItemRead0 newsItemRead0) {
            this.newsItemRead0 = newsItemRead0;
        }

        public Integer getId() {
            return newsItemRead0.getId();
        }

        // @override
        public String toString() {
            String result = "[" + newsItemRead0.getId() + "]";
            Date published = newsItemRead0.getPublished();
            Date publishedDate = newsItemRead0.getPublished();
            if (publishedDate != null) {
                String publishedText = new SimpleDateFormat("yyyy/MM/dd").format(publishedDate);
                result += " <" + publishedText + ">";
            }
            String title = newsItemRead0.getTitle();
            if (title != null)
                result += "\n" + title;

            return result;
        }
    }

    protected ArrayAdapter<NewsItemStringable> listAdapter;
    protected ArrayList<NewsItemStringable> newsItems;
    protected TreeMap<Integer, Integer> newsId2listIndex;

    public FragmentList() {
        newsItems = new ArrayList<>();
        newsId2listIndex = new TreeMap<>();
    }

    public void setActivity(ActionBarActivity activity, FetchMode fetchMode) {
        this.fetchMode = fetchMode;
        this.activity = activity;
        newsAction = (NewsAction0)activity;
    }

    public void loadInitial() {
        newsAction.get(fetchMode, 20, 0);
    }

    public void update(FetchMode fetchMode, ArrayList<NewsItemRead0> newsItemsMore) {
        if (fetchMode != this.fetchMode)
            return;

        int len = newsItemsMore.size();
        if (len == 0)
            return;

        for(NewsItemRead0 newsItem : newsItemsMore) {
            // TODO: validate that the order is correct
            // TODO: issue: ArrayList probably sucks WRT to insertion?
            Integer id = newsItem.getId();
            if (newsId2listIndex.containsKey(id)) {
                newsItems.set(newsId2listIndex.get(id), new NewsItemStringable(newsItem));
            } else {
                newsId2listIndex.put(id, newsItems.size());
                newsItems.add(new NewsItemStringable(newsItem));
            }
        }

        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    public void displayError(FetchError0 fetchError) {

    }

    public void loadMore() {
        newsAction.get(fetchMode, 10, newsItems.size());
    }

    protected class OnScrollListener implements AbsListView.OnScrollListener {
        protected FragmentList parent;
        OnScrollListener(FragmentList parent) {
            this.parent = parent;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen == totalItemCount)
                parent.loadMore();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView)view.findViewById(R.id.newslistView);
        listView.setOnScrollListener(new OnScrollListener(this));

        listAdapter = new ArrayAdapter<>(activity.getApplicationContext(),
                                android.R.layout.simple_list_item_1, newsItems);

        listView.setAdapter(listAdapter);
        return view;
    }
}
