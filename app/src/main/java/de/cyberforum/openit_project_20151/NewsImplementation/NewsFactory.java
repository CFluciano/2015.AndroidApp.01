package de.cyberforum.openit_project_20151.NewsImplementation;

import java.util.ArrayList;
import java.util.Date;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchReaction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemStatus0;
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
        int step = 0;
        Object params = null;
        switch(fetchMode) {
            case FM__ALL__PUBLISHED_DESC:
                // 1. check if we already got the "oldest" available news:
                //    if yes, exit; otherwise continue
                // [correct solution]
                // 2. read the timestamp for previous "oldest" from storage
                // [incorrect, lazy solution]
                // 2. take the timestamp of the "oldest" news in the cache
                // [even more incorrect, lazy solution]
                // 2. take the timestamp of the news with the smallest id

                // [most incorrect]
                // 2. just ask newsCache for the news on the "bottom"
                ArrayList<NewsItemStatus0> idList = newsCache.getIds(fetchMode, 1, -1);
                if ((idList != null) && (idList.size() == 1)) {
                    NewsItemStatus0 newsItemStatus = idList.get(0);
                    ArrayList<NewsItemRead0> items = newsCache.getItems(idList);
                    if ((items != null) && (items.size() == 1)) {
                        NewsItemRead0 item = items.get(0);
                        Date publishedJava = item.getPublished();
                        Long publishedUnix = - (publishedJava.getTime() / 1000);
                        params = publishedUnix;
                    }
                }

                break;
        }

        newsFetchAction.initiateFetch(fetchMode, step, params);

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
