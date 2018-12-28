package com.wikav.teahcer.teacherApp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

public class BottomNavigationViewHelper {
   private static int i=0,j=0,k=0;
    public static void enableNavigation(final Context context, BottomNavigationView view){
       view.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.home:



                       Intent intent1 = new Intent( context, HomeMenuActivity.class );
                       intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       context.startActivity( intent1 );


                       // j=k=false;



                       break;
                   case R.id.subject:

                       //item.set;
                       Intent intent2 = new Intent( context, SubjectActivity_2.class );
                       intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       context.startActivity( intent2 );



                       break;
                   case R.id.profile:

                       //Intent openIntent = new Intent(context, NewProfile.class);
//                           openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           context.startActivity(openIntent);
                       Intent intent5 = new Intent( context, NewProfile.class );
                       intent5. setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       context.startActivity( intent5 );



                       break;


               }
               return false;
           }
       } );
    }
}
