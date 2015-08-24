package de.cyberforum.openit_project_20151.NewsInterface;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsImplementation.FetchMode;

/**
 * Created by Balzer on 24.07.2015.
 */
public interface NewsFetchAction0 {
    // returns: null if ok, otherwise error object
    FetchError0 initiateFetch(FetchMode fetchMode, int step, Object params);
}
