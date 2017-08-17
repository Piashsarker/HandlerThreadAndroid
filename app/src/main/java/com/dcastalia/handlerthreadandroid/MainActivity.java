package com.dcastalia.handlerthreadandroid;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private ImageView imageView  ;
    private TextView text ;
    private Bitmap bitmap = null ;
    private final String Url = "https://developer.android.com/design/media/hero-material-design.png";
    private ProgressDialog progressDialog ;



    /** Seperate Handler is Used With handleMessage method
     * for updating the imageView and for dismissing the dialog
     * **/

    private Handler handler = new Handler(){
      public void handleMessage(Message message){
          super.handleMessage(message);
          if(bitmap!=null){
              imageView.setImageBitmap(bitmap);
              progressDialog.dismiss();
              Toast.makeText(MainActivity.this, "Download Complete!", Toast.LENGTH_SHORT).show();

          }
          else{
              progressDialog.dismiss();
              Toast.makeText(MainActivity.this, "Bitmap Not Found", Toast.LENGTH_SHORT).show();
          }


      }


    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

    }

    /** Find Views By Their ID **/
    private void findViews() {
        progress = (ProgressBar) findViewById(R.id.progressBar1);
        text = (TextView) findViewById(R.id.textView1);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    /** Onclick Method For Listening Button OnClick **/
    public void startProgress(View view ){

        /** Do Something Long Running Operation
         *  in Runnable Run Method
         *  **/

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                      for(int i=0 ; i<=10 ; i++){
                          final int value=i ;
                          doLongRunningWork();
                          progress.post(new Runnable() {
                              @Override
                              public void run() {
                                text.setText("Updating");
                                  progress.setProgress(value);
                              }
                          });
                      }
            }
        };

        new Thread(runnable).start();
    }


    private void doLongRunningWork() {
        /** Running the task for 5 seconds for example **/
        SystemClock.sleep(5000);

    }


    /** This method listen the onClick of Download Image **/
    public void downloadImage(View view){

        progressDialog = ProgressDialog.show(MainActivity.this,"","Loading...");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                bitmap = downloadBitmap(Url);
                handler.sendEmptyMessage(0);
            }
        };

        new Thread(runnable).start();

    }

    private Bitmap downloadBitmap(String url) {

        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return null;
        }

    }


}
