package com.wikav.teahcer.teacherApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wikav.teahcer.teacherApp.adapters.RecyclerViewAdapter;
import com.wikav.teahcer.teacherApp.model.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wikav.teahcer.teacherApp.Spash_Screen.PERMISSIONS_MULTIPLE_REQUEST;

@SuppressLint("NewApi")
public class HomeMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private final String JSON_URL = "https://gist.githubusercontent.com/aws1994/f583d54e5af8e56173492d3f60dd5ebf/raw/c7796ba51d5a0d37fc756cf0fd14e54434c547bc/anime.json" ;

Config config;
    //  public  adaptorRecycler;
    boolean doubleBackToExitPressedOnce = false;
    // LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    //private JsonArrayRequest request;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private List<Anime> lstAnime;

    private String school_id,sclPic;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefresh;

    private RecyclerViewAdapter adaptorRecycler;
LinearLayout img;
    BottomNavigationView bottomNavigationView;
    SessionManger sessionManger;
    // private final String URL_PRODUCTS = "http://schooli.000webhostapp.com/android/getPostData.php";
    private final String URL_PRODUCTS = "https://schoolian.website/android/getPostData.php";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManger = new SessionManger(this);
        sessionManger.checkLogin();

        config=new Config(this);

        config.CheckConnection();
//        linearLayout = findViewById(R.id.top);
//        linearLayout.setEnabled(false);

        setUpBottomNavigationView();
        HashMap<String, String> user = sessionManger.getUserDetail();
        String Ename = user.get(sessionManger.SCL_ID);
        String ame = user.get(sessionManger.SCL_pic);
        //Toast.makeText(this, ""+ame, Toast.LENGTH_SHORT).show();
        school_id = Ename;
        sclPic = ame;

        swipeRefresh = findViewById(R.id.swipe_refresh);

        final SwipeRefreshLayout.OnRefreshListener swpie = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lstAnime.clear();
                demofeed(school_id);
            }
        };
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //if internet is available
                lstAnime.clear();
                demofeed(school_id);
                //else
                //swipe_refresh.setRefreshing(false);
            }
        });
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
              swipeRefresh.setRefreshing(true);
               swpie.onRefresh();
            }
        });
         checkAndroidVersion();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        img=view.findViewById(R.id.scl_info);
        TextView txt=view.findViewById(R.id.scl_head);
        // Toast.makeText(this, scl_pic, Toast.LENGTH_SHORT).show();
        // img.setBackgroundResource(R.drawable.drawer);

        Glide.with(HomeMenuActivity.this).load(sclPic).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                img.setBackground(resource);
            }
        });

        txt.setText("");

        //for feed part or adaptor part or recycle par


///////////////////////////////////////////////////////////getting the recyclerview from xml//////////////////////////


//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        recyclerView = (RecyclerView) findViewById(R.id.recycleerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstAnime = new ArrayList<>();




        final String response2 = SessionManger.getString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY);

        try {
            parseHomeFeed(response2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // loadProducts();
        // jsonrequest();
    }

    private void parseHomeFeed(String response) throws JSONException {

        // stopProgressBar();

        JSONObject jsonObject1 = new JSONObject(response);
        String success = jsonObject1.getString("success");
        JSONArray jsonArray = jsonObject1.getJSONArray("userPost");

        if (success.equals("1")) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Anime anime = new Anime();

                anime.setName(jsonObject.getString("st_name"));

                anime.setCategorie(jsonObject.getString("posts"));
                anime.setId(jsonObject.getString("post_id"));
                anime.setStudio(jsonObject.getString("profile_pic"));
                anime.setSId(jsonObject.getString("sid"));
                anime.setTime(jsonObject.getString("time"));
                anime.setImage_url(jsonObject.getString("post_pic"));
                lstAnime.add(anime);

            }
            setuprecyclerview(lstAnime);
        }
        else {
            Toast.makeText(this, "No one posted", Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
        }

    }

    private void demofeed(final String id) {


        //showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //stopProgressBar();
                    SessionManger.putString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY, response);

                    final String response2 = SessionManger.getString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY);

                    parseHomeFeed(response2);

                    //save home response to shared preference


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeMenuActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeMenuActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", id);

                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setuprecyclerview(List<Anime> userPostsList) {

        if (adaptorRecycler == null) {
            adaptorRecycler = new RecyclerViewAdapter(this, userPostsList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adaptorRecycler);
        } else {
            adaptorRecycler.changeDataSet(userPostsList);
            adaptorRecycler.notifyDataSetChanged();
        }

        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }


    private void showProgress() {

        progressDialog = new ProgressDialog(HomeMenuActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    private void stopProgressBar() {

        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


//    @Override
  /*  public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeMenuActivity.this, NotificationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_camera2:
                Intent intent = new Intent(HomeMenuActivity.this, MyPost_NevActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_camera:
                Intent intent1 = new Intent(HomeMenuActivity.this, Schoolina_NevActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_gallery:
                Intent intent2 = new Intent(HomeMenuActivity.this, Message_NevActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_manage:
                Intent intent3 = new Intent(HomeMenuActivity.this, ContactUs_NevActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_slideshow:
                Intent intent4 = new Intent(HomeMenuActivity.this, Feedback_NevActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_send:
                Intent intent5 = new Intent(HomeMenuActivity.this, Help_NevActivity.class);
                startActivity(intent5);

                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // method for bottom navigation view
    private void setUpBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        BottomNavigationViewHelper.enableNavigation(HomeMenuActivity.this, bottomNavigationView);

    }


    public void feedPost(View view) {

        Intent intent = new Intent(HomeMenuActivity.this, feedUpload.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        config.CheckConnection();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            //  Toast.makeText(this, "Starting...", Toast.LENGTH_SHORT).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(HomeMenuActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(HomeMenuActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (HomeMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (HomeMenuActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(HomeMenuActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MEDIA_CONTENT_CONTROL,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            // Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile)
                    {
                        // write your logic here
                    } else {
                        Snackbar.make(HomeMenuActivity.this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                    PERMISSIONS_MULTIPLE_REQUEST);
                                        }
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        config.CheckConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        config.CheckConnection();

    }

}
