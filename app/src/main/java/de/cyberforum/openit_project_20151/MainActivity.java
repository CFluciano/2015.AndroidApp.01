package de.cyberforum.openit_project_20151;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.cyberforum.openit_project_20151.NewsImplementation.FetchMode;
import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsItemRead0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIAction0;
import de.cyberforum.openit_project_20151.NewsInterface.NewsUIReaction0;

import de.cyberforum.openit_project_20151.NewsImplementation.NewsUI;


public class MainActivity extends ActionBarActivity implements NewsAction0, NewsUIReaction0 {

    protected NewsUI newsUI;
    protected Fragment fragment;

    public ArrayList<NewsItemRead0> get(FetchMode fetchMode, Integer itemCount, Integer itemOffset) {
        return newsUI.get(fetchMode, itemCount, itemOffset);
    }

    public void update(FetchMode fetchMode, ArrayList<NewsItemRead0> news) {
        if (fragment != null) {
            NewsUIReaction0 newsUIReaction0 = (NewsUIReaction0)fragment;
            newsUIReaction0.update(fetchMode, news);
        }
    }

    public void displayError(FetchError0 fetchError) {
        if (fragment != null) {
            NewsUIReaction0 newsUIReaction0 = (NewsUIReaction0)fragment;
            newsUIReaction0.displayError(fetchError);
        }
    }

    protected int optionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws ClassCastException {
        super.onCreate(savedInstanceState);
        newsUI = new NewsUI(this);

        setContentView(R.layout.activity_main);

        optionId = 0;
        changeFragment(R.id.action_top5);
    }

    protected boolean changeFragment(int optionIdNew) {
        if (optionIdNew == optionId)
            return true;

        switch(optionIdNew) {
            case R.id.action_top5: {
                    FragmentTop5 fragmentTop5 = new FragmentTop5();
                    fragmentTop5.setActivity(this);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment, fragmentTop5);
                    ft.commit();

                    NewsUIAction0 newsUIAction = (NewsUIAction0) newsUI;
                    newsUIAction.activityChangeFragment(FetchMode.FM_TOP, fragmentTop5);

                    fragment = fragmentTop5;

                    fragmentTop5.loadInitial();
                }
                break;

            case R.id.action_list: {
                FragmentList fragmentList = new FragmentList();
                fragmentList.setActivity(this, FetchMode.FM__ALL__PUBLISHED_DESC);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment, fragmentList);
                ft.commit();

                NewsUIAction0 newsUIAction = (NewsUIAction0) newsUI;
                newsUIAction.activityChangeFragment(FetchMode.FM__ALL__PUBLISHED_DESC, fragmentList);

                fragment = fragmentList;

                fragmentList.loadInitial();
            }
            break;

            default:
                return false;
        }

        optionId = optionIdNew;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (changeFragment(id))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
