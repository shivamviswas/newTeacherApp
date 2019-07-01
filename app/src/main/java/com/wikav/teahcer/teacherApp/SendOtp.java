package com.wikav.teahcer.teacherApp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wikav-pc on 9/13/2018.
 */

public class SendOtp {
   private String authkey = "235212AZpQkT0BmNm5b8be6ed";
    //Multiple mobiles numbers separated by comma
    private String mobiles ;
    //Sender ID,Wh
    // ile using route4 sender id should be 6 characters long.
    private String senderId = "SCLOTP";
    //Your message to send, Add URL encoding here.
    private String message=null;
    //define route
    private String route="4";
    private URLConnection myURLConnection=null;
    private URL myURL=null;
    private BufferedReader reader=null;

    //encoding message
   // private String encoded_message= URLEncoder.encode(message);

    //Send SMS API
    private String mainUrl2="http://api.msg91.com/api/v2/sendsms?&country=91";
    private String mainUrl="http://api.msg91.com/api/sendhttp.php?country=91";


    SendOtp(String mobile,String msg)
    {
        mobiles=mobile;
        message=msg;
    }



    public boolean sendOtp() {
    //Prepare parameter string
    StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("&sender=" + senderId);
        sbPostData.append("&route=" + route);
        sbPostData.append("&mobiles=" + mobiles);
    sbPostData.append("&authkey=" + authkey);
        sbPostData.append("&message=" + message);



//final string
    mainUrl = sbPostData.toString();

        boolean value=true;
        try {
            URL url = new URL(mainUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            System.out.println("work creating HTTP connection");

            assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
            value=true;
        } catch (IOException e) {
            System.err.println("Error creating HTTP connection");
            e.printStackTrace();
           // value=false;
            try {
                throw e;
            } catch (IOException e1) {
                e1.printStackTrace();
              value=false;
            }
        }

       return value;

}
}
