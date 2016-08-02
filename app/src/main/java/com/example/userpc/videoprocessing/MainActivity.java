package com.example.userpc.videoprocessing;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends AppCompatActivity implements CallBackListener{

    FFmpegMediaMetadataRetriever med;
    private static final int FILE_REQUEST=1;
    RelativeLayout firstactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstactivity= (RelativeLayout) findViewById(R.id.main_screen);
        med=new FFmpegMediaMetadataRetriever();
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("file/*"),FILE_REQUEST);
    }

    void openFile(Uri uri)
    {
        med.setDataSource(MainActivity.this,uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FILE_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                if(data!=null)
                {
                    Snackbar.make(firstactivity,"File open successful",Snackbar.LENGTH_LONG).show();
                    Uri uri=data.getData();
                    openFile(uri);
                    FrameSeparator frameSeparator=new FrameSeparator(med,MainActivity.this);
                    frameSeparator.execute();
                }
            }
            else
            {
                Snackbar.make(firstactivity,"File open was unsuccessful",Snackbar.LENGTH_LONG).show();
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onTaskCompleted() {

    }
}
