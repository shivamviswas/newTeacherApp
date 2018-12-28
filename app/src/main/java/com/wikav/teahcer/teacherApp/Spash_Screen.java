package com.wikav.teahcer.teacherApp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Spash_Screen extends AppCompatActivity {
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
        SessionManger sessionManger;
       ProgressBar progressBar;
       ImageView retry,logopic,sclfont;
       TextView tapto, pst;
    AlertDialog.Builder builder;
    private final String uplod="https://schoolian.website/android/getAppUpdate.php";
    private final String URL_PRODUCTS ="https://schoolian.website/android/getPostData.php";
    private final String URL = "https://schoolian.website/android/login.php";
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash__screen);
        // retry = findViewById(R.id.retry);
        // tapto = findViewById(R.id.tapto);
        logopic = findViewById(R.id.logo);
        sclfont = findViewById(R.id.sclFont);
        pst = findViewById(R.id.slogo);
        //   tapto = findViewById(R.id.tapto);
        //  ImageView imageView = findViewById(R.id.logo);
        sessionManger = new SessionManger(this);
        progressBar = findViewById(R.id.progressBar);

        config = new Config(this);

        if (config.haveNetworkConnection()) {
            logopic.animate().translationYBy(-200f).setDuration(1500);

            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                    sclfont.animate().alpha(1f).setDuration(2500);
                    pst.animate().alpha(1f).setDuration(2500);


                }

            }, 3 * 1000);
            updateApp();
        } else {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       finish();

    }




    public void updateApp(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                              JSONArray jsonArray = jsonObject.getJSONArray("update");
                              progressBar.setVisibility(View.GONE);

                            //  Toast.makeText(Spash_Screen.this, "Success!"+response, Toast.LENGTH_SHORT).show();
                              int code=0;
                              String link="",msg="";
                              if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    code = object.getInt("version_code");
                                    link = object.getString("updatelink").trim();
                                    msg = object.getString("msg").trim();
                                }

                                Checkupdate(code,link,msg);



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(Spash_Screen.this, "Try Again!"+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Spash_Screen.this, "Try Again!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("getupdate","1");
                params.put("app","teacher");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void Checkupdate(int cod, final String link,final String msg) {
        if(versionCode<cod)
    { progressBar.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(Spash_Screen.this);

        builder.setTitle("Update!");


        builder.setMessage(msg);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Spash_Screen.this, "Updating...", Toast.LENGTH_SHORT).show();
                String url = "https://"+link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        builder.setCancelable(false);
       final AlertDialog alertDialog= builder.create();
        builder.show();
        Spash_Screen.this.setFinishOnTouchOutside(false);


    }
    else {

        progressBar.setVisibility(View.VISIBLE);
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(5000);
//
//
//                    if (!sessionManger.isLoging()) {
//                        String s=SessionManger.SCL_ID;
//                        demofeed(s);
//                        Intent intent = new Intent(Spash_Screen.this, Login.class);
//                        startActivity(intent);
//
//                    } else {
//
//                        Intent intent = new Intent(Spash_Screen.this, HomeMenuActivity.class);
//                        startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        thread.start();

            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                   // Toast.makeText(Spash_Screen.this, "work", Toast.LENGTH_SHORT).show();
                    if (!sessionManger.isLoging()) {
                        String s=SessionManger.SCL_ID;

                        demofeed(s);
                       // Toast.makeText(Spash_Screen.this, "work2", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Spash_Screen.this, Login.class);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(Spash_Screen.this, HomeMenuActivity.class);
                        startActivity(intent);
                    }

                    // close this activity

                    finish();

                }

            }, 5*1000);

        }

    }

    private void demofeed(final String id) {


        //showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //stopProgressBar();
                SessionManger.putString(Spash_Screen.this, SessionManger.HOME_FEED_KEY, response);



            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(HomeMenuActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}

