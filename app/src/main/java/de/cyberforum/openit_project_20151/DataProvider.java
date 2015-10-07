package de.cyberforum.openit_project_20151;
import android.util.Log;
/**
 * Created by pappa on 18/08/2015.
 */
public class DataProvider {
    private String id_news;
    private String date_news;
    private String news;

    public String getNews() { return news; }

    public void setNews(String news) {
        this.news = news;
    }

    public String getDate_news() { return date_news; }

    public void setDate_news(String date_news) {
        this.date_news = date_news;
    }

    public String getId_news() { return id_news; }

    public void setId_news(String id_news) { this.id_news = id_news; }

    public DataProvider (String id_news,String date_news,String news)
    {
        this.id_news = id_news;
        this.date_news = date_news;
        this.news = news;
    }
}
