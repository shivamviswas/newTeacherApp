package com.wikav.teahcer.teacherApp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wikav-pc on 8/29/2018.
 */

public class BottomSheet extends BottomSheetDialogFragment {
    Button button;
    SessionManger sessionManger;
    EditText name,lastname,email,phone,bio,passowrd;
    ProgressBar progressBar;

    final String Url="http://schoolian.website/android/teacher/updateProfile.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet,container,false);
        sessionManger=new SessionManger(getActivity());
        HashMap<String, String> user=sessionManger.getUserDetail();
        final String Ename = user.get(sessionManger.NAME);
        String last = user.get(sessionManger.LASTNAME);
        String Eemail = user.get(sessionManger.EMAIL);
        final String Esid = user.get(sessionManger.TID);
        String Ebio = user.get(sessionManger.BIO);
        String mobile = user.get(sessionManger.MOBILE);
        String sclName = user.get(sessionManger.SCLNAME);
        String clasName = user.get(sessionManger.CLAS);
        String pass=user.get(sessionManger.PASS);
        final String sclId=user.get(sessionManger.SCL_ID);

        name=v.findViewById(R.id.upName);
        lastname=v.findViewById(R.id.upLastName);
        email=v.findViewById(R.id.upemail);
        phone=v.findViewById(R.id.upmobile);
        bio=v.findViewById(R.id.upbio);
        passowrd=v.findViewById(R.id.upPass);

        name.setText(Ename);
        lastname.setText(last);
        email.setText(Eemail);
        phone.setText(mobile);
        bio.setText(Ebio);
        passowrd.setText(pass);
        button=v.findViewById(R.id.submitUpdate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClick(name.getText().toString(),lastname.getText().toString(),email.getText().toString(),bio.getText().toString(),
                        passowrd.getText().toString(),phone.getText().toString(),sclId,Esid);
            }
        });


progressBar=v.findViewById(R.id.upprog);

        return v;
    }

    private void updateClick(final String name, final String last, final String email, final String bio, final String pass, final String phone, final String sclId, final String esid) {
       //progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("update");
/*          $index['name'] = $row['teacher_name'];
                    $index['email'] = $row['t_email'];
                    $index['sid'] = $row['tid'];
                    $index['phone'] = $row['t_mobile'];
                    $index['bio'] = $row['bio'];
                    $index['photo'] = $row['profile_pic'];
                    $index['scl_id'] = $row['scl_id'];
                    $index['pass'] = $row['password'];
                    $index['sclPic'] = $ro['sprofile'];*/

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String bio = object.getString("bio").trim();
                                    String sid = object.getString("sid").trim();
                                    String photo = object.getString("photo").trim();
                                    String scl_id = object.getString("school_id").trim();
                                    String phone = object.getString("phone").trim();
                                    String pass = object.getString("pass").trim();
//                                    sessionManger.editor.clear();
//                                    sessionManger.editor.commit();
                                    sessionManger.UpdateSession(name, email, bio, sid, photo, scl_id, phone,pass);
//                                    Intent intent = new Intent(Login.this, HomeMenuActivity.class);
//                                    //                    intent.putExtra("name",name);
////                            intent.putExtra("email",email);
//                                    startActivity(intent);
//                                    finish();
                                }


                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                              BottomSheet.super.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           // progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Try Again!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("scl_id", sclId);
                params.put("sid", esid);
                params.put("phone", phone);
                params.put("email", email);
                params.put("pass", pass);
                params.put("bio", bio);
                params.put("name", name);
                params.put("last", last);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}
