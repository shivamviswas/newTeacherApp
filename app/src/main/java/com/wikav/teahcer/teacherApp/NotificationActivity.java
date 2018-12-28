package com.wikav.teahcer.teacherApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification );
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent( NotificationActivity.this,HomeMenuActivity.class );
        startActivity( intent );
    }
}
