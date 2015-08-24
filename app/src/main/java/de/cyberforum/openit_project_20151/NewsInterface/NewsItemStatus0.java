package de.cyberforum.openit_project_20151.NewsInterface;

import java.util.Date;

/**
 * Created by Balzer on 24.07.2015.
 */
public interface NewsItemStatus0 {
    int getId();
    Date getModified();

    // not deleted and not before start-of-publication and not after end-of-publication
    Boolean getValid();
}
