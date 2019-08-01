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
import android.widget.Button;
import android.widget.EditText;
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
import com.wikav.teahcer.teacherApp.adapters.AdapterForMarks;
import com.wikav.teahcer.teacherApp.model.Marks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab2 extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView.LayoutManager layoutManager;
    private final String JSON_URL = "https://schoolian.website/android/teacher/getClasses.php";
    private final String JSON_URL2 = "https://schoolian.website/android/teacher/getSubject.php";
    private final String URL = "https://schoolian.website/android/teacher/getStudentWithClass.php";
    private final String URL2 = "https://schoolian.website/android/teacher/marks.php";
    // private final String URL = "https://192.168.43.188/web/android/teacher/getStudentWithClass.php";

    ArrayAdapter<String> dataAdapter;
    private List<Marks> lstAnime;
    private RecyclerView recyclerView;
    SessionManger sessionManger;
    String sclid, clas, sidi, subjectItem=null;
    View view;
    Context context;
    Spinner spinner, spinner2;
    List<String> clsases;
    List<String> Subject;
    String itemcls = null,examName;
    ProgressBar progressBar;
    AdapterForMarks adaptorRecycler;
    FloatingActionButton button;
    Button totalMarksBtn;
    EditText exam,marks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sessionManger = new SessionManger(context);
        HashMap<String, String> user = sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Ecls = user.get(sessionManger.CLAS);
        String Sid = user.get(sessionManger.TID);
        sclid = Esclid;
        sidi = Sid;
        clas = Ecls;
        Config config=new Config(getActivity());

        config.CheckConnection();

        // showMarks(sclid,clas,sidi);
        view = inflater.inflate(R.layout.tab2, container, false);
        progressBar = view.findViewById(R.id.progress5);
        spinner = view.findViewById(R.id.spinnerClass);
        spinner.setOnItemSelectedListener(this);
        spinner2 = view.findViewById(R.id.spinnerTest);
        totalMarksBtn = view.findViewById(R.id.btnMarks);
        exam = view.findViewById(R.id.editTestName);
        marks = view.findViewById(R.id.editMarks);
        spinner2.setOnItemSelectedListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewMarks);
        lstAnime = new ArrayList<>();
        clsases = new ArrayList<>();
        Subject = new ArrayList<>();

        getClasses(sclid);
        clsases.add("Select Class");
        Subject.add("Select Subject");

        totalMarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemcls.equals("Select Class")&& subjectItem==null || subjectItem.equals("Select Subject"))
                {
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(exam.getText().toString().isEmpty()&& marks.getText().toString().isEmpty())
                    {
                        Toast.makeText(context, "Please Enter Exam And Marks", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        examName=exam.getText().toString();
                        totalMarks();

                    }

                }

            }
        });

        button=view.findViewById(R.id.submitMarks);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitMarks();
            }
        });


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

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    private void setAdapterMethod2(List<String> clsases) {

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clsases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter);

    }

    public void totalMarks() {

        String totalmarks=marks.getText().toString();
        shocoments(sclid,itemcls,totalmarks);

    }

    private void submitMarks()
    {
        final JSONArray array = new JSONArray();

        for(Marks att : lstAnime) {

                try {

                    JSONObject object = new JSONObject();

                    object.put("SchoolId", sclid);
                    object.put("classId", itemcls);
                    object.put("sId", att.getStId());
                    object.put("subjec", subjectItem);
                    object.put("examName", examName);
                    object.put("stName", att.getStName());
                    object.put("obtainMarks", att.getObtainMarrks());
                    object.put("totalMarks", att.getTotalMarks());

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
                        Toast.makeText(getActivity(), "Marks Submitted", Toast.LENGTH_SHORT).show();
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
                param.put("marks", array.toString());
                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    private void shocoments(final String posId, final String classname, final String totalmarks) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("student");

                    if (success.equals("1")) {
                        lstAnime.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Marks anime = new Marks();
                            anime.setStName(jsonObject.getString("name"));
                            //  anime.setSubject(jsonObject.getString("subject"));
                            anime.setStId(jsonObject.getString("sid"));
                            anime.setTotalMarks(totalmarks);
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

    private void setuprecyclerview(List<Marks> userPostsList) {

        adaptorRecycler = new AdapterForMarks(getActivity(), userPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptorRecycler);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinnerClass) {
            itemcls = parent.getItemAtPosition(position).toString();
            if (!itemcls.equals("Select Class")) {
                clear();
                setSeubject(itemcls);
               /// Toast.makeText(getActivity(), itemcls, Toast.LENGTH_LONG).show();
            }
        } else if (spinner.getId() == R.id.spinnerTest) {
            subjectItem = parent.getItemAtPosition(position).toString();
            if (!subjectItem.equals("Select Subject")) {
               // Toast.makeText(getActivity(), itemcls, Toast.LENGTH_LONG).show();
            }
        }

        //

    }

    private void setSeubject(final String item) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("subject");
                        Subject.clear();
                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String classes = jsonObject.getString("subject");

                            Subject.add(classes);


                        }
                        setAdapterMethod2(Subject);
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
                param.put("class", item);

                return param;

            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void clear() {

        int size = lstAnime.size();
        if (size>0){
        lstAnime.clear();
            adaptorRecycler.notifyDataSetChanged();
        }
    }


}
