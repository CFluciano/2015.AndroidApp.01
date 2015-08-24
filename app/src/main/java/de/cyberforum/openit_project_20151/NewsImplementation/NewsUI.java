package de.cyberforum.openit_project_20151.NewsImplementation;

import android.app.Activity;
import android.app.Fragment;

import java.util.ArrayList;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIReaction0;

/**
 * Created by Balzer on 24.07.2015.
 */
public class NewsUI implements NewsAction0, NewsUIAction0, NewsUIReaction0 {
    protected Activity activity;
    protected Fragment fragment = null;
    protected NewsFactory newsFactory;

    protected NewsUIReaction0 uiReaction0;
    protected NewsAction0 newsFactoryAction0;
    protected FetchMode fetchMode = FetchMode.FM_NONE;

    public NewsUI(Activity activity) throws ClassCastException {
        this.activity = activity;
        newsFactory = new NewsFactory(this);

        uiReaction0 = (NewsUIReaction0)activity;
        newsFactoryAction0 = (NewsAction0)newsFactory;
    }

    public void activityChangeFragment(FetchMode fetchMode, Fragment fragmentNew) throws ClassCastException {
        this.fetchMode = fetchMode;
        fragment = fragmentNew;

        uiReaction0 = (NewsUIReaction0)fragment;
    }

    public ArrayList<NewsItemRead0> get(FetchMode fetchmode, Integer itemCount, Integer itemOffset) {
        this.fetchMode = fetchmode;
        return newsFactoryAction0.get(fetchmode, itemCount, itemOffset);
    }

    public void update(FetchMode fetchMode, ArrayList<NewsItemRead0> newsItems) {
        // if UI has changed, ignore queued update
        if (fetchMode != this.fetchMode)
            return;

        // integrate update into this.news
        uiReaction0.update(fetchMode, newsItems);
    }

    public void displayError(FetchError0 fetchError) {
        uiReaction0.displayError(fetchError);
    }
}
