package de.cyberforum.openit_project_20151;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import de.cyberforum.openit_project_20151.NewsImplementation.FetchMode;
import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIReaction0;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentTop5 extends Fragment implements NewsUIReaction0 {

    protected ActionBarActivity activity;
    protected NewsAction0 newsAction;

    public FragmentTop5() {
    }

    public void setActivity(ActionBarActivity activity) {
        this.activity = activity;
        newsAction = (NewsAction0)activity;
    }

    public void loadInitial() {
        newsAction.get(FetchMode.FM_TOP, 5, 0);
    }

    public void update(FetchMode fetchMode, ArrayList<NewsItemRead0> news) {
        int len = news.size() > 5 ? 5 : news.size();
        if (len == 0)
            return;

        String packageName = activity.getPackageName();
        for(int i = 0; i < len; i++) {
            int newsEntryId = getResources().getIdentifier("newsEntry" + (i + 1), "id", packageName);
            TextView textView = (TextView)getActivity().findViewById(newsEntryId);

            NewsItemRead0 item = news.get(i);
            String text = "[" + item.getId() + "]";
            Date publishedDate = item.getPublished();
            if (publishedDate != null) {
                String publishedText = new SimpleDateFormat("yyyy/MM/dd").format(publishedDate);
                text += " <" + publishedText + ">";
            }
            String title = item.getTitle();
            if (title != null)
                text += " " + title;
//            String subtitle = item.getSubtitle();
//            if (subtitle != null)
//                text += "\n" + subtitle;
            textView.setText(text);
        }
    }

    public void displayError(FetchError0 fetchError) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top5, container, false);
    }
}
