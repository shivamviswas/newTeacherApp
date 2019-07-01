package com.wikav.teahcer.teacherApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.teahcer.teacherApp.adapters.AdapterForSyllabus;
import com.wikav.teahcer.teacherApp.model.Syllabus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class tab3 extends Fragment implements AdapterView.OnItemSelectedListener {

    String selectedClassId=null;
    ArrayAdapter<String> dataAdapter;
    private final String JSON_URL_GET_Class = "https://schoolian.website/android/teacher/getClasses.php";
    View view;
    FloatingActionButton button;
    // Context
    Spinner spinner;
    List<String> clsases;

    Context context;
    SessionManger sessionManger;
    String sclid, tid;
    private List<Syllabus> lstAnime;
    private RecyclerView recyclerView;
    AdapterForSyllabus adaptorRecycler;
    ProgressBar progressBar;

    private final String JSON_URL = "https://schoolian.website/android/teacher/getSyl.php";
    private final String URL2 = "https://schoolian.website/android/teacher/syllabus.php";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab3, container, false);
        Config config=new Config(getActivity());

        config.CheckConnection();
        sessionManger = new SessionManger(context);
        HashMap<String, String> user = sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Sid = user.get(sessionManger.TID);
        sclid = Esclid;
        tid=Sid;
        recyclerView=view.findViewById(R.id.recyclerViewSyllabus);
        lstAnime=new ArrayList<>();
        spinner = view.findViewById(R.id.spinnerClassSyl);
        spinner.setOnItemSelectedListener(this);
        progressBar = view.findViewById(R.id.progressSyllabus);
        clsases=new ArrayList<>();
        clsases.add("Select Class");
        getClasses(sclid);
        button=view.findViewById(R.id.submitSyllabus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedClassId.equals(null)||selectedClassId.equals("Select Class"))
                {
                    Toast.makeText(getActivity(), "Please Select Class", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitSyllabus();
                }

            }
        });

        return view;

    }

    private void showSub(final String sclid, final String tid, final String selectedClassId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("subjects");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Syllabus anime = new Syllabus();
                            anime.setSubject(jsonObject.getString("subject"));
                            anime.setTotalSyllabus(jsonObject.getString("totalSyllabus"));
                            anime.setSubjectId(jsonObject.getString("sub_id"));
                            anime.setSyllabusComplete(jsonObject.getString("lastSyllabus"));
                            lstAnime.add(anime);
                        }
                        setuprecyclerview(lstAnime);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", sclid);
                param.put("tid", tid);
                param.put("class", selectedClassId);

                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }
    private void setuprecyclerview(List<Syllabus> userPostsList) {

        adaptorRecycler = new AdapterForSyllabus(getActivity(), userPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptorRecycler);


    }


    private void getClasses(final String sclid) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_GET_Class, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("className");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String classes = jsonObject.getString("className");

                            clsases.add(classes);


                        }
                        setAdapterMethod(clsases);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", sclid);

                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void setAdapterMethod(List<String> clsases) {

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clsases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedClassId = parent.getItemAtPosition(position).toString();
        if (!selectedClassId.equals("Select Class")) {
            showSub(sclid,tid,selectedClassId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void submitSyllabus()
    {
        final JSONArray array = new JSONArray();

        for(Syllabus att : lstAnime) {

            try {

                JSONObject object = new JSONObject();

                object.put("SchoolId", sclid);
                object.put("sub_id", att.getSubjectId());
                object.put("syllabus", att.getSyllabusComplete());
                array.put(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("request : ", array.toString());
        submitDataRequest(array);

    }

    private void submitDataRequest(final JSONArray array) {

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(getActivity(), "Syllabus Submitted", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(),HomeMenuActivity.class);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("syllabus", array.toString());
                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


}
