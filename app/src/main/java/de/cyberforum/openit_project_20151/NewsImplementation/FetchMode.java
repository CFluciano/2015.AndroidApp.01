package de.cyberforum.openit_project_20151.NewsImplementation;

/**
 * Created by Balzer on 24.07.2015.
 */
public enum FetchMode {
    FM_NONE,                    // in error contexts: actual mode not available
    FM_TOP,                     // top 5
    FM__ALL__PUBLISHED_DESC     // standard view: active news, descending by "published"-date
}
