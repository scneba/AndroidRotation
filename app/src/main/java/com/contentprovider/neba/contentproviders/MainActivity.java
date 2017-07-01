package com.contentprovider.neba.contentproviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contentprovider.neba.contentproviders.models.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks<JSONArray> {


    private final int LOADER_ID = 10;
    private TextView textView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    private ArrayList<Data> raw_data_list;
    private ArrayList<Data> datalist_from_saved_instance;
    private DataAdapter dataAdapter;
    int track_rotations=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         progressBar = (ProgressBar) findViewById(R.id.pbload);
        progressBar.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.textView);



        recyclerView = (RecyclerView) findViewById(R.id.myrecycler);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        raw_data_list=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        Bundle loader_bundle = new Bundle();

            loader_bundle.putString("url", "http://mysafeinfo.com/api/data?list=englishmonarchs&format=json");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if(savedInstanceState!=null && savedInstanceState.containsKey("alldata")){

            raw_data_list =  savedInstanceState.getParcelableArrayList("alldata");
            dataAdapter = new DataAdapter(raw_data_list,this);
            recyclerView.setAdapter(dataAdapter);

        }else
            {
                dataAdapter = new DataAdapter(raw_data_list,this);
                recyclerView.setAdapter(dataAdapter);
            //initialize loader
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<JSONArray> loader = loaderManager.getLoader(LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(LOADER_ID, loader_bundle, this).forceLoad();
            } else {
                loaderManager.restartLoader(LOADER_ID, loader_bundle, this).forceLoad();
            }
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("alldata", raw_data_list);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<JSONArray>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {

                    return;
                }
             progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public JSONArray loadInBackground() {
                String url = args.getString("url");
                Log.e("url",url);
                if (url == null || url.isEmpty()) {
                    return null;
                }
                try {
                    HttpRequest httpRequest = new HttpRequest(url);
                   return httpRequest.prepare(HttpRequest.Method.GET, 10000, 10000).sendAndReadJSONArray();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        progressBar.setVisibility(View.GONE);
        raw_data_list.clear();
        if(data!=null){
            for(int i = 0; i<data.length(); i++){
                try {
                    JSONObject  jsonObject = data.getJSONObject(i);
                    Log.e("data1",jsonObject.toString());
                    raw_data_list.add(new Data(jsonObject.getString("nm"),jsonObject.getString("cty"),jsonObject.getString("hse"),jsonObject.getString("yrs")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataAdapter.notifyDataSetChanged();

            }

        }

    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }
}
