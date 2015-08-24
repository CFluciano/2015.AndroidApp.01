package de.cyberforum.openit_project_20151.NewsInterface;

import java.util.Date;

/**
 * Created by Balzer on 24.07.2015.
 */
public interface NewsItemRead0 extends NewsItemStatus0 {
    Date getPublished();
    String getTitle();      // plain text, UTF-8
    String getSubtitle();   // plain text, UTF-8
    String getBody();       // HTML, UTF-8
}
