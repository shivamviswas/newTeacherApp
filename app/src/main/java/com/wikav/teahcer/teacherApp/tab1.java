package com.wikav.teahcer.teacherApp;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import  com.wikav.teahcer.teacherApp.adapters.AdapterForAttednce;
import com.wikav.teahcer.teacherApp.model.Attendence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab1 extends Fragment implements AdapterView.OnItemSelectedListener {


    private final String JSON_URL = "https://schoolian.website/android/teacher/getClasses.php";
    private final String URL = "https://schoolian.website/android/teacher/getStudentWithClass.php";
    private final String URL2 = "https://schoolian.website/android/teacher/attendence.php";
    // private final String JSON_URL = "https://192.168.43.188/web/android/teacher/getClasses.php";
    // private final String URL = "https://192.168.43.188/web/android/teacher/getStudentWithClass.php";

    RequestQueue queue;
    String selectedClassId=null;
    ArrayAdapter<String> dataAdapter;
    AdapterForAttednce adaptorRecycler;
    private List<Attendence> lstAnime;
    private RecyclerView recyclerView;
    SessionManger sessionManger;
    ProgressBar progressBar;
    String sclid = "";
    View view;
    FloatingActionButton button;
    // Context
    Spinner spinner;
    List<String> clsases;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        sessionManger = new SessionManger(getActivity());
        Config config=new Config(getActivity());

        config.CheckConnection();
        HashMap<String, String> user = sessionManger.getUserDetail();
        String Esid = user.get(sessionManger.SCL_ID);

        sclid = Esid;
        ///shocoments(sclid);
        view = inflater.inflate(R.layout.tab1, container, false);
        button=view.findViewById(R.id.submitAtt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedClassId.equals(null)||selectedClassId.equals("Select Class"))
                {
                    Toast.makeText(getActivity(), "Please Select Class", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitAttendanceData();
                }

            }
        });

        spinner = view.findViewById(R.id.spinnerClass);
        spinner.setOnItemSelectedListener(this);
        progressBar = view.findViewById(R.id.progressBar4);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTeacher);
        lstAnime = new ArrayList<>();
        clsases = new ArrayList<String>();
        getClasses(sclid);
        clsases.add("Select Class");

        queue = Volley.newRequestQueue(getActivity());
        return view;

    }

    private void getClasses(final String sclid) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
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
        spinner.setAdapter(dataAdapter);

    }




    private void shocoments(final String posId, final String classname) {

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("student");
                    lstAnime.clear();
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Attendence anime = new Attendence();
                            anime.setStundentName(jsonObject.getString("name"));
                            //  anime.setSubject(jsonObject.getString("subject"));
                            anime.setStId(jsonObject.getString("sid"));
                            anime.setPresent(true);
//                            anime.setTimOf(jsonObject.getString("subject_time"));
//                            anime.setImage_url_teacher(jsonObject.getString("profile_pic"));
//                           // lstAnime = new ArrayList<Attendence>();

                            lstAnime.add(anime);

                        }
                        setuprecyclerview(lstAnime);
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
                param.put("school_id", posId);
                param.put("class", classname);

                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void submitAttendanceData() {

        final JSONArray array = new JSONArray();

        for(Attendence att : lstAnime) {

            try {

                JSONObject object = new JSONObject();

                object.put("SchoolId", sclid);
                object.put("classId", selectedClassId);
                object.put("sId", att.getStId());
                object.put("stName", att.getStundentName());
                object.put("att", att.isPresent() ? "P" : "A");

                array.put(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("request : ", array.toString());

        submitDataRequest(array);
//        JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, url, array,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> param = new HashMap<>();
//                param.put("request", String.valueOf(array));
//                return param;
//            }
//        };
//
//        queue.add(jobReq);
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
                        Log.d("Attend",array.toString());
                        Toast.makeText(getActivity(), "Attendance Submitted", Toast.LENGTH_SHORT).show();
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
                param.put("attend", array.toString());
                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    private void setuprecyclerview(List<Attendence> userPostsList) {


        adaptorRecycler = new AdapterForAttednce(getActivity(), userPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptorRecycler);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedClassId = parent.getItemAtPosition(position).toString();
        if (!selectedClassId.equals("Select Class")) {
            shocoments(sclid, selectedClassId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
