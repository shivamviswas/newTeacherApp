package com.wikav.teahcer.teacherApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ProfileActivity_5 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ImageView imageView;
    Integer REQUEST_CAMERA=1,SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile_5 );
        setUpBottomNavigationView();

        imageView = findViewById( R.id.image );

    }

    public void galery(View view){

        selectImage();


    }
    private void selectImage(){
        final CharSequence []item = {"Camera","Galery"};
        AlertDialog.Builder builder = new AlertDialog.Builder( ProfileActivity_5.this );
        builder.setTitle( "Add Image" );
        builder.setItems( item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item[which].equals( "Camera" )){
                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                    startActivityForResult( intent,REQUEST_CAMERA );

                }else if(item[which].equals( "Galery" )) {
                    Intent intent1 = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    intent1.setType( "image/*" );
                    startActivityForResult( intent1.createChooser( intent1, "Select File" ), SELECT_FILE );
                }
            }
        } );builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap =(Bitmap) bundle.get( "data" );
                imageView.setImageBitmap( bitmap );

            }else if(requestCode==SELECT_FILE){
                Uri uri = data.getData();
                imageView.setImageURI( uri );

            }
        }
    }

    private void setUpBottomNavigationView(){
         bottomNavigationView = findViewById( R.id.bottomnavigationview );
        BottomNavigationViewHelper.enableNavigation( ProfileActivity_5.this,bottomNavigationView );

    }
    public void onBackPressed() {
        Intent intent = new Intent( ProfileActivity_5.this,HomeMenuActivity.class );
        startActivity( intent );
        finish();
    }
}
