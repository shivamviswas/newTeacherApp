package com.wikav.teahcer.teacherApp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditText name,lastName,password,classUP,school,mobile,email,bioet;
    private ProgressBar progressBar;
    private SessionManger sessionManger;
    Button up1,up2,up3,up4,up5,up6,up7;
    private String sname,slastName,spassword,sclassUP,sschool,smobile,semail,bio,id,Url="http://schoolian.in/android/updateProfile.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        up1=findViewById(R.id.up1);
        up2=findViewById(R.id.up2);
        up3=findViewById(R.id.up3);
        up4=findViewById(R.id.up4);
        up5=findViewById(R.id.up5);
        up6=findViewById(R.id.up6);
        up7=findViewById(R.id.up7);
        bioet=findViewById(R.id.Etbio);
        school=findViewById(R.id.Etschool_id);
        name = findViewById(R.id.Etsname);
        lastName=findViewById(R.id.Etlastname);
        email=findViewById(R.id.Etemail);
        mobile=findViewById(R.id.Etnumber);
        password=findViewById(R.id.Etpassword);
        classUP=findViewById(R.id.Etclss);
        progressBar=findViewById(R.id.upprog);

        sessionManger=new SessionManger(this);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Ename = user.get(sessionManger.NAME);
       String Eemail = user.get(sessionManger.EMAIL);
       String Esid = user.get(sessionManger.TID);
       String Ebio = user.get(sessionManger.BIO);
       String cls = user.get(sessionManger.CLAS);
        String lst = user.get(sessionManger.LASTNAME);
        String scl = user.get(sessionManger.SCL_ID);
        String mob = user.get(sessionManger.MOBILE);

       sname=Ename;
       slastName=lst;
       sclassUP=cls;
       sschool=scl;
       smobile=mob;
       semail=Eemail;
       bio=Ebio;
       id=Esid;

        school.setText(sschool);
        school.setClickable(false);
        classUP.setText(sclassUP);
        mobile.setText(smobile);
        email.setText(semail);
        name.setText(sname);
        bioet.setText(bio);
        lastName.setText(slastName);

    }


    public void classUP(View view) {
        final String sid=id;
        int n=1;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(classUP.getText()),n);

    }

    public void LastName(View view) {
        final String sid=id;
        int n=3;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(lastName.getText()),n);
    }

    public void NameUp(View view) {

        final String sid=id;
        int n=2;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(name.getText()),n);
    }

    public void bioUp(View view) {
        final String sid=id;
        int n=6;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(bioet.getText()),n);
    }

    public void mobileNum(View view) {

        final String sid=id;
        int n=4;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(mobile.getText()),n);

    }

    public void emailUp(View view) {

        final String sid=id;
        int n=5;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(email.getText()),n);

    }

    public void passwordUp(View view) {
        if(String.valueOf(password.getText()).isEmpty() || String.valueOf(password.getText()).equals("")||String.valueOf(password.getText())==null )
        {
            password.setError("please fill");
        }
        else
        {
        final String sid=id;
        int n=7;
        progressBar.setVisibility(View.VISIBLE);
        profileUpadate(sid,String.valueOf(password.getText()),n);}

    }


    private void profileUpadate(final String sid, final String sclassUP, final int n) {

        progressBar.setVisibility(View.VISIBLE);
       // submit.setVisibility(View.GONE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1"))
                    {
                       // Toast.makeText(StudentRegistrationActivity.this, "Email Already Exist",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        switch (n){
                            case 1:
                                sessionManger.setClas(jsonObject.getString("class"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your Class has successfully Changed !",Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                sessionManger.setProName(jsonObject.getString("name"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your Name has successfully Changed !",Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                sessionManger.setLastname(jsonObject.getString("lastname"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your lastname has successfully Changed !",Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                sessionManger.setProPhone(jsonObject.getString("phone"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your phone has successfully Changed !",Toast.LENGTH_LONG).show();
                                break;

                            case 5:
                                sessionManger.setProEmail(jsonObject.getString("email"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your email has successfully Changed !",Toast.LENGTH_LONG).show();

                                break;
                            case 6:
                                sessionManger.setProBio(jsonObject.getString("bio"));
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your bio has successfully Changed !",Toast.LENGTH_LONG).show();

                                break;
                            case 7:
                                up1.setBackgroundColor(Color.parseColor("#3ED715"));
                                Toast.makeText(EditProfile.this, "Your Password has successfully Changed !",Toast.LENGTH_LONG).show();
                                break;

                        }



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(StudentRegistrationActivity.this, "Register Error"+e.toString(),Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);

                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      // Toast.makeText(StudentRegistrationActivity.this, "Register Error 2:"+error.toString(),Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> pram=new HashMap<>();
                pram.put("sid",sid);
                pram.put("check",String.valueOf(n));
                pram.put("update",sclassUP);

                return pram;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




    }



}
