
package de.cyberforum.openit_project_20151.NewsImplementation;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsFetchReaction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Balzer on 24.07.2015.
 */
public class NewsFetch implements NewsFetchAction0 {

    protected NewsFetchReaction0 newsFetchReaction;

    public NewsFetch(NewsFetchReaction0 newsFetchReaction) {
        this.newsFetchReaction = newsFetchReaction;
    }

    public FetchError0 initiateFetch(FetchMode fetchMode, int step, Object params) {
        // null: request was queued successfully, otherwise: error object
        // TODO: check for network
        // TODO: put into fetch queue
        startFetch(fetchMode, step, params);
        return null;
    }

    protected ArrayList<AsyncHttpClient> clients;

    protected void startFetch(FetchMode fetchMode, int step, Object params) {
        TextHttpResponseHandlerEx handler = new TextHttpResponseHandlerEx();
		handler.setOwnArguments(fetchMode, step, params, newsFetchReaction);

        String url = "http://www.cyberforum.de/cybermobile.php";
        if (fetchMode == FetchMode.FM__ALL__PUBLISHED_DESC) {
            if (params instanceof Long) {
                Long param = (Long) params;
                if (param < 0) {
                    param = -param;
                    url += "?method=olderIDThan&parameters=" + param.toString();
                } else
                    url += "?method=newerIDThan&parameters=" + param.toString();
            } else if (params instanceof Integer) {
                Integer param = (Integer) params;
                if (param < 0) {
                    param = -param;
                    url += "?method=smallerIDThan&parameters=" + param.toString();
                } else
                    url += "?method=greaterIDThan&parameters=" + param.toString();
            } else {
                // storage completely empty or unknown params
                Long now = System.currentTimeMillis() / 1000;
                url += "?method=olderIDThan&parameters=" + now.toString();
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        // client.setBasicAuth("<login>", "<pwd>");
        client.get(url, handler);
    }

	protected class TextHttpResponseHandlerEx extends TextHttpResponseHandler {

        protected FetchMode fetchMode;
        protected int step;
        protected Object params;
		protected NewsFetchReaction0 newsFetchReaction;

		public void setOwnArguments(FetchMode fetchMode, int step, Object params, NewsFetchReaction0 newsFetchReaction) {
            this.fetchMode = fetchMode;
            this.step = step;
            this.params = params;
			this.newsFetchReaction = newsFetchReaction;
		}

		@Override
        public void onSuccess(String responseBody) {
            switch(this.fetchMode) {
                case FM_TOP:
                    onSuccessTop5(responseBody);
                    break;

                case FM__ALL__PUBLISHED_DESC:
                    onSuccessAllPubDesc(responseBody);
            }
        }

        protected void onSuccessTop5(String responseBody) {
            JSONArray response;

            try {
                response = new JSONArray(responseBody);
            } catch (JSONException e) {
                FetchError fetchError = new FetchError(fetchMode, "foo", "bar");
                newsFetchReaction.fetchError(fetchError);
                return;
            }

            int len = response.length();
            if (len > 0) {
                ArrayList<NewsItemRead0> newsItems = new ArrayList<>();
                for(int i = 0; i < len; i++) {
                    JSONObject line;
                    try {
                        line = response.getJSONObject(i);
                    } catch (JSONException e) {
                        break;
                    }

                    Integer id = null;
                    try {
                        id = line.getInt("newsId");
                    } catch (JSONException e) {
                        break;
                    }

                    NewsItem newsItem = new NewsItem(id, null, null);

                    Long datetimeUnix;
                    Date datetimeJava;
                    String title;
                    String subtitle;
                    try {
                        datetimeUnix = line.getLong("at");
                        title = line.getString("newsTitle");
                        subtitle = line.getString("summary");
                        datetimeJava = new Date(datetimeUnix * 1000);
                        newsItem.NewsItemWrite(datetimeJava, title, subtitle, null);
                    } catch (JSONException e) {
                        // ignore any failure here
                    }

                    newsItems.add(newsItem);
                }

                if (newsItems.size() > 0)
                    newsFetchReaction.fetchResult(fetchMode, newsItems);
            }
        }

        protected void onSuccessAllPubDesc(String responseBody) {
            JSONObject container;
            JSONArray response;

            try {
                container = new JSONObject(responseBody);
                if (!container.has("idList")) {
                    FetchError fetchError = new FetchError(fetchMode, "foo", "bar");
                    newsFetchReaction.fetchError(fetchError);
                    return;
                }

                response = container.getJSONArray("idList");
            } catch (JSONException e) {
                FetchError fetchError = new FetchError(fetchMode, "foo", "bar");
                newsFetchReaction.fetchError(fetchError);
                return;
            }

            int len = response.length();
            if (len > 0) {
                ArrayList<NewsItemRead0> newsItems = new ArrayList<>();
                for(int i = 0; i < len; i++) {
                    Integer id;
                    try {
                        id = response.getInt(i);
                    } catch (JSONException e) {
                        break;
                    }

                    NewsItem newsItem = new NewsItem(id, null, null);
                    newsItems.add(newsItem);
                }

                if (newsItems.size() > 0)
                    newsFetchReaction.fetchResult(fetchMode, newsItems);
            }
		}

        @Override
        public void onFailure(String responseBody, Throwable e) {
            FetchError fetchError = new FetchError(fetchMode, "foo", "bar");
            newsFetchReaction.fetchError(fetchError);
		}
	}

    protected class NewsItem implements NewsItemRead0 {
        NewsItem(int id, Date modified, Boolean valid) {
            this.id = id;
            this.modified = modified;
            this.valid = true;
        }
        protected int id;
        public int getId() { return id; }
        protected Date modified;
        public Date getModified() { return modified; }

        // not deleted and not before start-of-publication and not after end-of-publication
        protected Boolean valid;
        public Boolean getValid() { return valid; }

        public void NewsItemWrite(Date published, String title, String subtitle, String body) {
            this.published = published;
            this.title = title;
            this.subtitle = subtitle;
            this.body = body;
        }

        protected Date published;
        protected String title;
        protected String subtitle;
        protected String body;
        public Date getPublished() { return published; }
        public String getTitle() { return title; }          // plain text, UTF-8
        public String getSubtitle() { return subtitle; }    // plain text, UTF-8
        public String getBody() { return body; }            // HTML, UTF-8
    }
}
