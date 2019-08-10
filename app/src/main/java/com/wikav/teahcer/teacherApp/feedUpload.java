package com.wikav.teahcer.teacherApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;

import android.database.Cursor;

import android.net.Uri;

import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

//import com.android.volley.request.SimpleMultiPartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class feedUpload extends AppCompatActivity {
    private ImageView viewImage;
    private  Button imageSelect,uploadBtn;
    ImageButton imageButton;
    private EditText postText;
    SessionManger sessionManger;
    public String getId,scl_Id;
    Bitmap newImage,thumbnail;
    int i=0;
    String uplod="https://schoolian.in/android/teacher/post_upload.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_upload);
        viewImage=(ImageView)findViewById(R.id.postImage2);
        uploadBtn= findViewById(R.id.postUp);
        imageSelect=findViewById(R.id.choosIm);
        imageButton=findViewById(R.id.imageButton);
        postText=findViewById(R.id.postText);
        sessionManger = new SessionManger(this);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String getid = user.get(sessionManger.TID);
        String scl = user.get(sessionManger.SCL_ID);
        getId=getid;
        scl_Id=scl;

        Toast.makeText(getApplicationContext(), ""+scl_Id, Toast.LENGTH_SHORT).show();
        imageButton=findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage.setImageBitmap(null);
                viewImage.setVisibility(View.GONE);
                imageSelect.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                i=0;
            }
        });

        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                final String et=postText.getText().toString().trim();

                if(et.isEmpty()&&i==0)
                {
                    postText.setError("Please Enter Query Or Select Image");
                }
                else {

                    if (i == 0) {
                        //
                        UploadOnlyWrite(getId, et);

                    } else if (et.isEmpty() && i == 1) {
                        // Toast.makeText(feedUpload.this, "et worrk", Toast.LENGTH_SHORT).show();
                        UploadOnlyPicture(getId, getStringImage(newImage));
                    } else {
                        //Toast.makeText(feedUpload.this, "waorklslsls", Toast.LENGTH_SHORT).show();
                        UploadWithPicture(getId, et, getStringImage(newImage));
                    }

                }
            }

        });

    }
/////////////////////////////////////////////////////////////// UPLOAD WITH PICTURE//////////////////////////////////////////////////////////////////////////////////


    private void UploadWithPicture(final String id, final String et, final String stringImage) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUpload.this.setFinishOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUpload.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUpload.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",id);
                params.put("post_pic",stringImage);
                params.put("posts",et);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

/////////////////////////////////////////////////////////////// UPLOAD ONLY TEXT//////////////////////////////////////////////////////////////////////////////////

    private void UploadOnlyWrite(final String id, final String et) {
        final String BLANK="";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUpload.this.setFinishOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUpload.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUpload.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",id);
                params.put("posts",et);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }



    /////////////////////////////////////////////////////////////// UPLOAD PICTURE ONLY//////////////////////////////////////////////////////////////////////////////////
    private void UploadOnlyPicture(final String id, final String photo) {
        final String BLANK="";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUpload.this.setFinishOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUpload.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUpload.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", id);
                params.put("post_pic", photo);
                params.put("posts", BLANK);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }



    @Override
    public void onBackPressed() {

        Intent intent =new Intent(feedUpload.this,HomeMenuActivity.class);
        startActivity(intent);
        finish();

    }


    public void chooseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }



    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                Toast.makeText(getApplicationContext(), "Okk", Toast.LENGTH_SHORT).show();

                try {
                    newImage = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    viewImage.setVisibility(View.VISIBLE);
                    viewImage.setImageBitmap(newImage);
                    i=1;
                    imageButton.setVisibility(View.VISIBLE);
                    imageSelect.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.i("Photo",error.toString());
            }
        }
    }
}
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                thumbnail = (Bitmap) extras.get("data");
                imageSelect.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                viewImage.setImageBitmap(thumbnail);
                i=1;

            }

            else if (requestCode == 2) {



                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                thumbnail = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", picturePath+"");

                //UploadPicture(getId, getStringImage(thumbnail));
                viewImage.setVisibility(View.VISIBLE);
                imageSelect.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                viewImage.setImageBitmap(thumbnail);
                i=1;

            }

        }

    }
*/

