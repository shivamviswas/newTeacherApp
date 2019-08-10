package com.wikav.teahcer.teacherApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Random;


public class StudentRegistrationActivity extends AppCompatActivity {
    EditText name,stId,phone,pass,otpEnter;
    Button button,varifybutton,resendbutton;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    TextView mobileTT,sidTT;
    private final String Url="https://schoolian.in/android/teacher/checking.php";
    private final String Url2="https://schoolian.in/android/teacher/teacherRegister.php";
    boolean i=false;
    int Black = Color.parseColor("#000000"),otpStore;
    String Name,Mobiles,Sid,Pass,otpValue;
    Random rnd;
    SessionManger sessionManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_student_registration );
        Config config=new Config(this);

        config.CheckConnection();
        name = findViewById( R.id.name );
        stId = findViewById( R.id.stId );
        pass = findViewById( R.id.pass );
        phone = findViewById( R.id.mobile );
        mobileTT = findViewById( R.id.mobilett );
        sidTT = findViewById( R.id.sidtt );
        otpEnter = findViewById( R.id.otpEnter );
        linearLayout=findViewById( R.id.otplayout );
        relativeLayout=findViewById( R.id.mainlayout );
        sessionManger=new SessionManger(this);
        button = findViewById( R.id.button );
        varifybutton=findViewById( R.id.varifybutton );


        stId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String s=stId.getText().toString().trim();
                if(!hasFocus){
                    if(!s.isEmpty()){
                        i=true;
                        checkSid(s);

                    }
                }
                else {
                    if(i) {
                        stId.setTextColor(Black);
                        sidTT.setText("School ID");
                        button.setEnabled(true);
                    }
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String s=phone.getText().toString().trim();
                if(!hasFocus){
                    if(isValidPhoneNumber(s)){
                        i=true;
                        checkMobile(s);
                    }
                    else {
                        i=true;
                        mobileTT.setText("Mobile Number: Invalid Mobile");
                        int color = Color.parseColor("#FF0000");
                        phone.setTextColor(color);
                        button.setEnabled(false);
                    }
                }
                else {
                    if(i) {
                        phone.setTextColor(Black);
                        mobileTT.setText("Mobile Number");
                        button.setEnabled(true);
                    }
                }
            }
        });

        rnd = new Random();
        otpStore = rnd.nextInt(999999) + 100000;
        //Otpsend=new SendOtp();
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name=name.getText().toString();
                Pass=pass.getText().toString().trim();
                if(!Mobiles.isEmpty())
                {
                    String msg="Your OTP is "+otpStore+".Thank You.";
                    SendOtp otp=new SendOtp(Mobiles,msg);
                    if (otp.sendOtp())
                    {
                        Toast.makeText(StudentRegistrationActivity.this, "OTP Sending...", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility( View.INVISIBLE );
                    }

                }

            }
        } );
        varifybutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpValue=otpEnter.getText().toString().trim();
                if (!otpValue.isEmpty())
                {
                    if(otpStore==Integer.parseInt(otpValue))
                    {
                        sendData(Sid,Mobiles,Pass,Name);
                    }
                    else {
                        Toast.makeText(StudentRegistrationActivity.this, "Invalid OTP!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } );

/**/
    }

    private void sendData(final String sid, final String mobiles, final String pass, final String name) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name").trim();
                            String email = object.getString("email").trim();
                            String bio = object.getString("bio").trim();
                            String sid = object.getString("sid").trim();
                            String photo = object.getString("photo").trim();
                            String scl_id = object.getString("scl_id").trim();
                            // String sclname = object.getString("scl_name").trim();
                            String phone = object.getString("phone").trim();
                            String password = object.getString("pass").trim();
                            String sclPic = object.getString("sclPic").trim();

                            sessionManger.createSession(name,email,bio,sid,photo,scl_id,phone,password,sclPic);
                            Intent intent = new Intent(StudentRegistrationActivity.this, HomeMenuActivity.class);
                            //                    intent.putExtra("name",name);
//                            intent.putExtra("email",email);
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentRegistrationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegistrationActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> pram=new HashMap<>();
                pram.put("scl_id",sid);
                pram.put("phone",mobiles);
                pram.put("name",name);
                pram.put("pass",pass);
                return pram;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void checkMobile(final String Mobile) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    //String msg = jsonObject.getString("massage");
                    if(success.equals("2"))
                    {
                        mobileTT.setText("Mobile Number: Already Register");
                        int color = Color.parseColor("#FF0000");
                        phone.setTextColor(color);
                        button.setEnabled(false);
                    }
                    else if(success.equals("1")) {
                        Mobiles=Mobile;
                        button.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentRegistrationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegistrationActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> pram=new HashMap<>();
                pram.put("phone",Mobile);
                return pram;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void checkSid(final String sid) {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String msg = jsonObject.getString("massage");
                    if (success.equals("1"))
                    {
                        sidTT.setText("School ID:"+msg);
                        Sid=sid;
                        button.setEnabled(true);
                    }
                    else if(success.equals("2"))
                    {
                        sidTT.setText("School ID: Invalid ID");
                        int color = Color.parseColor("#FF0000");
                        stId.setTextColor(color);
                        button.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentRegistrationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegistrationActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> pram=new HashMap<>();
                pram.put("scl_id",sid);
                return pram;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void resendOtp(View view) {
        otpStore = rnd.nextInt(999999) + 100000;
        String msg="Your OTP is "+otpStore+".Thank You.";
        SendOtp otp=new SendOtp(Mobiles,msg);
        if (otp.sendOtp())
        {
            Toast.makeText(StudentRegistrationActivity.this, "OTP Sending...", Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility( View.INVISIBLE );
        }
    }

    public static boolean isValidPhoneNumber(String target) {
        if (target.equals("")||target.isEmpty())  {
            return false;
        } else {

            if(target.charAt(0)!='6'&&target.charAt(0)!='7'&&target.charAt(0)!='8'&&target.charAt(0)!='9') {
                return false;
            }
            else {
                if(target.length() < 10 ){
                    return false;
                }
                else {
                    return android.util.Patterns.PHONE.matcher(target).matches();
                }
            }

        }

    }

}
