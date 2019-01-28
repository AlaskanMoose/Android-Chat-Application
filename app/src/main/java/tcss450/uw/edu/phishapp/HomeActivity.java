package tcss450.uw.edu.phishapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.phishapp.SetLists.SetList;
import tcss450.uw.edu.phishapp.blog.BlogPost;
import tcss450.uw.edu.phishapp.model.Credentials;
import tcss450.uw.edu.phishapp.utils.GetAsyncTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SuccessFragment.OnSuccessFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        BlogFragment.OnBlogListFragmentInteractionListener,
        BlogPostFragment.OnFragmentInteractionListener,
        WaitFragment.OnWaitFragmentInteractionListener,
        SetListFragment.OnListFragmentInteractionListener,
        SetListItemFragment.OnFragmentInteractionListener {

    private String mJwToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        loadSuccess();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fragment_success) {
            loadSuccess();
            // Handle the camera action
        } else if (id == R.id.fragment_blog) {
//            loadFragment(new BlogFragment());
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_blog))
                    .appendPath(getString(R.string.ep_get))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleBlogGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();
        } else if (id == R.id.fragment_setList){
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_setlists))
                    .appendPath(getString(R.string.ep_recent))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleSetListGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();
//            loadFragment(new SetListFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Credentials credentials) {

//        SuccessFragment successFragment = new SuccessFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("key", credentials);
//        successFragment.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.longDate.frame_main_container, successFragment);
//
//        transaction.commit();

    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null); //// remove this adding to backstack.
        // Commit the transaction
        transaction.commit();
    }

    private void loadSuccess() {
        Credentials credentials = (Credentials)getIntent()
                .getExtras().getSerializable(getString(R.string.keys_intent_credentials));

        mJwToken = getIntent().getStringExtra(getString(R.string.keys_intent_jwt));

        SuccessFragment successFragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.keys_intent_credentials), credentials);
        successFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, successFragment);

        transaction.commit();
    }

    private void handleBlogGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));

                    List<BlogPost> blogs = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);

                        blogs.add(new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build());
                    }

                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
                    blogsAsArray = blogs.toArray(blogsAsArray);


                    Bundle args = new Bundle();
                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
                    Fragment frag = new BlogFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleSetListGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_setlist_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_setlist_response));
                if (response.has(getString(R.string.keys_json_setlist_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));

                    List<SetList> sets = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonSetList = data.getJSONObject(i);

                        sets.add(new SetList.Builder(
                                jsonSetList.getString(
                                        getString(R.string.keys_json_setList_longdate)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_setList_location)))
                                .addVenue(jsonSetList.getString(
                                        getString(R.string.keys_json_setList_venue)))
                                .addSetListData(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_setlistdata)))
                                .addSetListNotes(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_setlistnotes)))
                                .addUrl(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_url)))
                                .build());
                    }

                    SetList[] setsAsArray = new SetList[sets.size()];
                    setsAsArray = sets.toArray(setsAsArray);


                    Bundle args = new Bundle();
                    args.putSerializable(SetListFragment.ARG_SET_LIST, setsAsArray);
                    Fragment frag = new SetListFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }


    @Override
    public void onBlogListInteraction(BlogPost item) {

        BlogPostFragment blogpost = new BlogPostFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_blogPost), item);
        blogpost.setArguments(args);
        loadFragment(blogpost);

    }

    @Override
    public void onBlogFragmentInteraction(BlogPost item) {


    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }


    @Override
    public void onRegisterSuccess(Credentials credentials) {

    }


    @Override
    public void onListFragmentInteraction(SetList item) {

        SetListItemFragment setListItemFragment = new SetListItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_setList), item);
        setListItemFragment.setArguments(args);
        loadFragment(setListItemFragment);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
