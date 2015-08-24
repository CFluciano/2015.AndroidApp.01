package de.cyberforum.openit_project_20151.NewsImplementation;

import java.util.ArrayList;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchReaction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIReaction0;

/**
 * Created by Balzer on 24.07.2015.
 */
public class NewsFactory implements NewsAction0, NewsFetchReaction0 {
    protected NewsCache newsCache;
    protected NewsFetch newsFetch;
    protected NewsFetchAction0 newsFetchAction;

    protected NewsUIReaction0 newsUIReaction;

    public NewsFactory(NewsUIReaction0 newsUIReaction) throws ClassCastException {
        this.newsUIReaction = newsUIReaction;
        newsCache = new NewsCache();
        newsFetch = new NewsFetch(this);
        newsFetchAction = (NewsFetchAction0)newsFetch;
    }

    public ArrayList<NewsItemRead0> get(FetchMode fetchMode, Integer itemCount, Integer itemOffset) {
        // read from cache
        ArrayList<NewsItemRead0> cachedData = null;

        // initiate fetch:
        // TODO: depending on type and itemOffset, do specific fetch
        // TODO: remember what was fetched, so it is not fetched again
        newsFetchAction.initiateFetch(fetchMode, 0, null);

        return cachedData;
    }

    public void fetchResult(FetchMode fetchmode, ArrayList<NewsItemRead0> newsList) {
        // store in cache

        // send update to UI
        newsUIReaction.update(fetchmode, newsList);
    }

    public void fetchError(FetchError0 fetchError) {
        // depending on error object determine further course of action
        newsUIReaction.displayError(fetchError);
    }

}
